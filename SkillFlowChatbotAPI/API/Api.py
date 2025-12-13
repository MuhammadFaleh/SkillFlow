from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import ChatBot.Model as Model
from pydantic import BaseModel

llm_rag = Model.RAGApplication(Model.pdf_retriever, Model.chain)
llm_skill_recommendation = Model.SkillRecommendationApplication(Model.sql_retriever, Model.skill_recommendation_chain) if Model.sql_exists else None
llm_employee_matching = Model.EmployeeMatchingApplication(Model.sql_retriever, Model.employee_matching_chain) if Model.sql_exists else None
llm_training_recommendation = Model.TrainingRecommendationApplication(Model.sql_retriever, Model.training_recommendation_chain) if Model.sql_exists else None

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)


class QueryRequest(BaseModel):
    question: str
    company_id: int = None


class SkillRecommendationRequest(BaseModel):
    company_id: int = None
    project_id: int = None


class EmployeeMatchingRequest(BaseModel):
    question: str
    company_id: int = None
    project_id: int = None


class TrainingRecommendationRequest(BaseModel):
    company_id: int = None
    employee_id: int = None


class SkillRecommendationResponse(BaseModel):
    answer: str


class EmployeeMatchingResponse(BaseModel):
    answer: str


class TrainingRecommendationResponse(BaseModel):
    answer: str


@app.post("/ask-rag")
async def query_rag(request: QueryRequest):
    try:
        answer = llm_rag.run(request.question, request.company_id)
        return {"answer": answer}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.post("/recommend-skills", response_model=SkillRecommendationResponse)
async def recommend_skills(request: SkillRecommendationRequest):
    if not llm_skill_recommendation:
        raise HTTPException(status_code=503, detail="SQL database not loaded")
    try:
        answer = llm_skill_recommendation.run(request.company_id, request.project_id)
        return {"answer": answer}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.post("/match-employees", response_model=EmployeeMatchingResponse)
async def match_employees(request: EmployeeMatchingRequest):
    if not llm_employee_matching:
        raise HTTPException(status_code=503, detail="SQL database not loaded")
    try:
        answer = llm_employee_matching.run(request.question, request.company_id, request.project_id)
        return {"answer": answer}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.post("/recommend-training", response_model=TrainingRecommendationResponse)
async def recommend_training(request: TrainingRecommendationRequest):
    if not llm_training_recommendation:
        raise HTTPException(status_code=503, detail="SQL database not loaded")
    try:
        answer = llm_training_recommendation.run(request.company_id, request.employee_id)
        return {"answer": answer}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "pdf_db_loaded": Model.pdf_exists,
        "sql_db_loaded": Model.sql_exists,
        "skill_recommendation_available": llm_skill_recommendation is not None,
        "employee_matching_available": llm_employee_matching is not None,
        "training_recommendation_available": llm_training_recommendation is not None
    }