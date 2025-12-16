from typing import List

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import ChatBot.model as model
import API.request as request
import API.response as response

llm_rag = model.RAGApplication(model.pdf_retriever, model.chain)
llm_skill_recommendation = model.SkillRecommendationApplication(model.sql_retriever, model.skill_recommendation_chain)
llm_employee_matching = model.EmployeeMatchingApplication(model.sql_retriever, model.employee_chain)
llm_training_recommendation = model.TrainingRecommendationApplication(model.sql_retriever, model.llm)

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)


@app.post("/ask-rag", response_model=response.QueryResponse)
async def query_rag(request: request.QueryRequest):
    try:
        answer = llm_rag.run(request.question, request.company_id)
        return {"answer": answer}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.post("/recommend-skills", response_model=List[response.SkillRecommendation])
async def recommend_skills(request: request.SkillRecommendationRequest):
    if not llm_skill_recommendation:
        raise HTTPException(status_code=503, detail="SQL database not loaded")
    try:
        answer = llm_skill_recommendation.run(request.company_id, request.project_id)
        return answer
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.post("/match-employees", response_model=List[response.EmployeeMatch])
async def match_employees(request: request.EmployeeMatchingRequest):
    if not llm_employee_matching:
        raise HTTPException(status_code=503, detail="SQL database not loaded")
    try:
        answer = llm_employee_matching.run(request.company_id, request.project_id)
        return answer
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.post("/recommend-training", response_model=response.TrainingRecommendationResponse)
async def recommend_training(request: request.TrainingRecommendationRequest):
    if not llm_training_recommendation:
        raise HTTPException(status_code=503, detail="Training recommendation system not loaded")
    try:
        emp_info, skills, training = llm_training_recommendation.run(request.company_id, request.employee_id)

        return {
            "emp_info": emp_info,
            "skills": skills,
            "training": training,
        }
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "pdf_db_loaded": model.pdf_exists,
        "sql_db_loaded": model.sql_exists,
        "skill_recommendation_available": llm_skill_recommendation is not None,
        "employee_matching_available": llm_employee_matching is not None,
        "training_recommendation_available": llm_training_recommendation is not None
    }