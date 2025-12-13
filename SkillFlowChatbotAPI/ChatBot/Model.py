import os

import chromadb
import torch
from langchain_community.tools import QuerySQLDataBaseTool
from langchain_community.utilities import SQLDatabase

import ChatBot.Ingest as Ingest
from langchain_chroma import Chroma
import re
import ChatBot.Constants as Constants
from langchain_ollama import OllamaLLM
from langchain_core.output_parsers import StrOutputParser

from torch import cuda, bfloat16

device = f'cuda:{cuda.current_device()}' if cuda.is_available() else 'cpu'

pdf_exists, pdf_db = Ingest.get_vectorstore(db_type='pdf')
sql_exists, sql_db = Ingest.get_vectorstore(db_type='sql')

if pdf_exists and sql_exists:
    pdf_retriever = pdf_db.as_retriever(k=4)
    sql_retriever = sql_db.as_retriever(k=10)

hf_auth = os.getenv('HUGGINGFACEHUB_API_TOKEN')

llm = OllamaLLM(
    model=Constants.MODEL,
    num_gpu=1,
    temperature=0,
    num_ctx=4096,
    num_batch=512,
    num_thread=8,
    gpu_layers=999
)

chain = Constants.prompt | llm | StrOutputParser()
skill_recommendation_chain = Constants.skill_recommendation_prompt | llm | StrOutputParser()
employee_matching_chain = Constants.employee_matching_prompt | llm | StrOutputParser()
training_recommendation_chain = Constants.training_recommendation_prompt | llm | StrOutputParser()


class RAGApplication:
    def __init__(self, retriever, rag_chain):
        self.retriever = retriever
        self.rag_chain = rag_chain

    def run(self, question, company_id=None):
        search_kwargs = {"k": 4}
        if company_id:
            search_kwargs["filter"] = {"company_id": {"$eq": company_id}}

        documents = self.retriever.invoke(question, **search_kwargs)
        doc_texts = "\n".join([doc.page_content for doc in documents])
        answer = self.rag_chain.invoke({"question": question, "documents": doc_texts})
        return answer


class EmployeeMatchingApplication:
    def __init__(self, retriever, chain):
        self.retriever = retriever
        self.chain = chain

    def run(self, question, company_id=None, project_id=None):
        project_filter = {}
        if project_id:
            project_filter = {
                "$and": [
                    {"type": {"$eq": "project"}},
                    {"id": {"$eq": project_id}}
                ]
            }
            if company_id:
                project_filter["$and"].append({"company_id": {"$eq": company_id}})
        elif company_id:
            project_filter = {
                "$and": [
                    {"type": {"$eq": "project"}},
                    {"company_id": {"$eq": company_id}}
                ]
            }

        project_docs = self.retriever.invoke(question, k=5, filter=project_filter) if project_filter else []

        employee_filter = {
            "$and": [
                {"type": {"$eq": "employee"}},
                {"company_id": {"$eq": company_id}}
            ]
        } if company_id else {"type": {"$eq": "employee"}}

        employee_docs = self.retriever.invoke(question, k=20, filter=employee_filter)

        all_documents = project_docs + employee_docs
        doc_texts = "\n\n".join([doc.page_content for doc in all_documents])

        answer = self.chain.invoke({
            "question": question,
            "documents": doc_texts
        })
        return answer


class SkillRecommendationApplication:
    def __init__(self, retriever, chain):
        self.retriever = retriever
        self.chain = chain

    def run(self, company_id=None, project_id=None):
        project_filter = {
            "$and": [
                {"type": {"$eq": "project"}},
                {"id": {"$eq": project_id}},
                {"company_id": {"$eq": company_id}}
            ]
        } if project_id and company_id else {}

        project_docs = self.retriever.invoke("project details", k=3, filter=project_filter) if project_filter else []

        # Step 2: Get ALL available skills from company
        skill_filter = {
            "$and": [
                {"type": {"$eq": "skill"}},
                {"company_id": {"$eq": company_id}}
            ]
        } if company_id else {"type": {"$eq": "skill"}}

        skill_docs = self.retriever.invoke("available skills", k=20, filter=skill_filter)

        all_documents = project_docs + skill_docs
        doc_texts = "\n\n".join([doc.page_content for doc in all_documents])
        answer = self.chain.invoke({"documents": doc_texts})
        return answer


class TrainingRecommendationApplication:
    def __init__(self, retriever, chain):
        self.retriever = retriever
        self.chain = chain

    def run(self, company_id=None, employee_id=None):
        # Step 1: Get specific employee
        employee_filter = {
            "$and": [
                {"type": {"$eq": "employee"}},
                {"id": {"$eq": employee_id}},
                {"company_id": {"$eq": company_id}}
            ]
        } if employee_id and company_id else {}

        employee_docs = self.retriever.invoke("employee profile", k=2,
                                              filter=employee_filter) if employee_filter else []

        project_filter = {
            "$and": [
                {"type": {"$eq": "project"}},
                {"company_id": {"$eq": company_id}},
                {"active": {"$eq": True}}
            ]
        } if company_id else {"type": {"$eq": "project"}, "active": {"$eq": True}}

        project_docs = self.retriever.invoke("active projects", k=10, filter=project_filter)

        skill_filter = {
            "$and": [
                {"type": {"$eq": "skill"}},
                {"company_id": {"$eq": company_id}}
            ]
        } if company_id else {"type": {"$eq": "skill"}}

        skill_docs = self.retriever.invoke("available skills", k=15, filter=skill_filter)

        training_filter = {
            "$and": [
                {"type": {"$eq": "training"}},
                {"company_id": {"$eq": company_id}}
            ]
        } if company_id else {"type": {"$eq": "training"}}

        training_docs = self.retriever.invoke("training programs", k=10, filter=training_filter)

        all_documents = employee_docs + project_docs + skill_docs + training_docs
        doc_texts = "\n\n".join([doc.page_content for doc in all_documents])
        answer = self.chain.invoke({"documents": doc_texts})
        return answer
# skill_app = SkillRecommendationApplication(sql_retriever, skill_recommendation_chain)
# matching_app = EmployeeMatchingApplication(sql_retriever, employee_matching_chain)
# training_app = TrainingRecommendationApplication(sql_retriever, training_recommendation_chain)