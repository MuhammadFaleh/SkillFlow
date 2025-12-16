from pydantic import BaseModel


class QueryRequest(BaseModel):
    question: str
    company_id: int = None


class SkillRecommendationRequest(BaseModel):
    company_id: int = None
    project_id: int = None


class EmployeeMatchingRequest(BaseModel):
    company_id: int = None
    project_id: int = None


class TrainingRecommendationRequest(BaseModel):
    company_id: int = None
    employee_id: int = None