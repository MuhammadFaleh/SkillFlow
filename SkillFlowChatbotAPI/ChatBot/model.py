import json
import ChatBot.ingest as ingest
import ChatBot.constants as constants
from langchain_ollama import OllamaLLM
from langchain_core.output_parsers import StrOutputParser

pdf_exists, pdf_db = ingest.get_vectorstore(db_type="pdf")
sql_exists, sql_db = ingest.get_vectorstore(db_type="sql")

pdf_retriever = pdf_db.as_retriever(search_kwargs={"k": 4}) if pdf_exists else None
sql_retriever = sql_db.as_retriever(search_kwargs={"k": 10}) if sql_exists else None

llm = OllamaLLM(
    model=constants.MODEL,
    num_gpu_layers=25,
)

chain = constants.prompt | llm | StrOutputParser()
skill_recommendation_chain = (constants.skill_recommendation_prompt | llm | StrOutputParser())

training_recommendation_chain = (constants.training_recommendation_prompt | llm | StrOutputParser())
employee_chain = (constants.employee_matching_prompt | llm | StrOutputParser())


class RAGApplication:
    def __init__(self, retriever, rag_chain):
        self.retriever = retriever
        self.rag_chain = rag_chain

    def run(self, question, company_id=None):
        search_kwargs = {"k": 4}
        if company_id:
            search_kwargs["filter"] = {"company_id": {"$eq": company_id}}

        documents = self.retriever.invoke(question, **search_kwargs)
        doc_texts = "\n".join(doc.page_content for doc in documents)

        return self.rag_chain.invoke({"question": question, "documents": doc_texts})


class EmployeeMatchingApplication:
    def __init__(self, retriever, chain):
        self.retriever = retriever
        self.chain = chain

    def run(self, company_id=None, project_id=None):
        project_filter = None
        if project_id:
            project_filter = {"$and": [{"type": {"$eq": "project"}}, {"id": {"$eq": project_id}}]}
            if company_id:
                project_filter["$and"].append({"company_id": {"$eq": company_id}})

        project_docs = self.retriever.invoke("project details", k=1, filter=project_filter) if project_filter else []

        employee_filter = {
            "$and": [{"type": {"$eq": "employee"}}, {"company_id": {"$eq": company_id}}]} if company_id else {
            "type": {"$eq": "employee"}}

        employee_docs = self.retriever.invoke("employee details", k=10, filter=employee_filter)

        all_docs = project_docs + employee_docs
        doc_texts = "\n".join(doc.page_content for doc in all_docs)

        output_str = self.chain.invoke({"documents": doc_texts})

        try:
            employees = json.loads(output_str)
        except json.JSONDecodeError as e:
            print(output_str)
            raise ValueError(f"failed to parse into json: {str(e)}")
        return employees


class SkillRecommendationApplication:
    def __init__(self, retriever, chain):
        self.retriever = retriever
        self.chain = chain

    def run(self, company_id=None, project_id=None):

        project_docs = []
        if company_id and project_id:
            project_docs = self.retriever.invoke(
                "project details",
                k=3,
                filter={
                    "$and": [
                        {"type": {"$eq": "project"}},
                        {"id": {"$eq": project_id}},
                        {"company_id": {"$eq": company_id}}
                    ]
                },
            )

        skill_filter = {
            "$and": [
                {"type": {"$eq": "skill"}},
                {"company_id": {"$eq": company_id}}
            ]
        }
        skill_docs = self.retriever.invoke("available skills", k=20, filter=skill_filter)

        doc_texts = "\n".join(doc.page_content for doc in (project_docs + skill_docs))

        raw_output = self.chain.invoke({"documents": doc_texts})
        output_str = raw_output.strip()
        if output_str.startswith("```"):
            output_str = output_str.split("```")[1].strip()
        try:
            skills_list = json.loads(output_str)
            if not isinstance(skills_list, list):
                raise ValueError("expected a JSON array from the model")
        except json.JSONDecodeError as e:
            raise ValueError(f"failed to parse into json: {str(e)}")

        return skills_list


class TrainingRecommendationApplication:
    def __init__(self, retriever, llm):
        self.retriever = retriever
        self.employee_info_chain = constants.employee_info_prompt | llm | StrOutputParser()
        self.skill_recommend_chain = constants.skill_emp_recommendation_prompt | llm | StrOutputParser()
        self.training_recommend_chain = constants.training_recommendation_prompt | llm | StrOutputParser()

    def run(self, company_id, employee_id):
        # 1. Get docs
        employee_docs = self.retriever.invoke(
            "employee profile",
            k=2,
            filter={
                "$and": [
                    {"type": {"$eq": "employee"}},
                    {"id": {"$eq": employee_id}},
                    {"company_id": {"$eq": company_id}},
                ]
            },
        )
        project_docs = self.retriever.invoke(
            "active projects",
            k=10,
            filter={
                "$and": [
                    {"type": {"$eq": "project"}},
                    {"company_id": {"$eq": company_id}},
                    {"active": {"$eq": True}},
                ]
            },
        )
        skill_docs = self.retriever.invoke(
            "available skills",
            k=15,
            filter={"type": {"$eq": "skill"}},
        )
        training_docs = self.retriever.invoke(
            "training programs",
            k=10,
            filter={"type": {"$eq": "training"}},
        )

        employee_docs_text = "\n".join(d.page_content for d in employee_docs)
        employee_info = self.employee_info_chain.invoke({"employee_docs_text": employee_docs_text})

        # 3. Recommend skills
        project_docs_text = "\n".join(d.page_content for d in project_docs)
        skill_docs_text = "\n".join(d.page_content for d in skill_docs)
        recommended_skills = self.skill_recommend_chain.invoke({
            "employee_info_json": employee_info,
            "project_docs_text": project_docs_text,
            "skill_docs_text": skill_docs_text
        })

        training_docs_text = "\n".join(d.page_content for d in training_docs)
        recommended_training_programs = self.training_recommend_chain.invoke({
            "recommended_skills_json": recommended_skills,
            "training_docs_text": training_docs_text
        })
        try:
            employee_info_json = json.loads(employee_info)
            recommended_skills_json = json.loads(recommended_skills)
            recommended_training_programs_json = json.loads(recommended_training_programs)
        except json.JSONDecodeError as e:
            raise ValueError(f"failed to parse into json: {str(e)}")

        return employee_info_json, recommended_skills_json, recommended_training_programs_json


if sql_exists:
    training_app = TrainingRecommendationApplication(sql_retriever, llm)

    employee_info_json, recommended_skills_json, recommended_training_programs_json = training_app.run(

        company_id=1,
        employee_id=1,
    )
    skill_match = SkillRecommendationApplication(sql_retriever, skill_recommendation_chain)
    r = skill_match.run(
        company_id=1,
        project_id=1,
    )
    print(r)
    print(employee_info_json)
    print(recommended_skills_json)
    print(recommended_training_programs_json)

    # training_app = EmployeeMatchingApplication(sql_retriever, employee_chain)
    #
    # r = training_app.run(
    #
    #     company_id=1,
    #     project_id=1,
    # )


