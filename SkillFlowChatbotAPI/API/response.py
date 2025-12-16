from pydantic import BaseModel
from typing import List, Optional


class QueryResponse(BaseModel):
    answer: str


class EmployeeMatch(BaseModel):
    employee_name: str
    employee_username: str
    match_score: int
    current_assignment: str
    matching_skills: List[str]
    missing_skills: Optional[List[str]]
    fit: Optional[str] = None


class EmployeeInfo(BaseModel):
    employee_profile_name: str
    employee_username: str
    current_skills: str | List[str]


class SkillRecommendation(BaseModel):
    skill_id: str
    priority: str
    justification: str


class TrainingRecommendation(BaseModel):
    training_name: str
    teaches_skill_id: str
    benefit: str


class TrainingRecommendationResponse(BaseModel):
    emp_info: EmployeeInfo
    skills: List[SkillRecommendation]
    training: List[TrainingRecommendation]