import os
from pathlib import Path

from dotenv import load_dotenv
from chromadb.config import Settings
from langchain_core.prompts import PromptTemplate
import chromadb
from langchain_huggingface import HuggingFaceEmbeddings

load_dotenv()
PROJECT_ROOT = Path(__file__).resolve().parent.parent

persist_directory = os.path.join(PROJECT_ROOT, os.environ.get('PERSIST_DIRECTORY'))


source_directory = os.environ.get('SOURCE_DIRECTORY', 'source_documents')
embeddings_id = os.environ.get('EMBEDDINGS_ID')

chunk_size = 1000
chunk_overlap = 100
embeddings = HuggingFaceEmbeddings(model_name=embeddings_id)
MODEL = os.getenv('MODEL_ID')


if persist_directory is None:
    raise Exception("Please set the PERSIST_DIRECTORY environment variable")

CHROMA_SETTINGS = Settings(
    anonymized_telemetry=False
)

prompt = PromptTemplate(
    template="""You are an assistant for question-answering tasks.
    Use the following documents to answer the question.
    If you don't know the answer, just say that you don't know.
    keep the answer concise:
    Question: {question}
    Documents: {documents}
    Answer:
    """,
    input_variables=["question", "documents"],
)
skill_recommendation_prompt = PromptTemplate(
    template="""You are a skill assignment advisor for projects.
    Use the following context about the project and available skills in the company.

    Project and Skills Context: {documents}

    Analyze the project description and identify 5-10 essential skills needed.
    For each skill provide:
    1. Skill name
    2. In short Why this skill is critical for the project

    Be specific and reference the project requirements from the context.
    Only suggest skills that exist in the company's skill database shown in the context.
    format the answer into a list with each skill and why it's needed 
    example:
    1- react, why it's needed:
    keep the answer concise:
    Answer:
    """,
    input_variables=["documents"],
)

employee_matching_prompt = PromptTemplate(
    template="""You are a project staffing advisor matching employees to projects.

Context: {documents}

Question: {question}

CRITICAL: Look at the employee's "Current Project" field. If it matches the project being staffed, DO NOT recommend that employee.

Analyze ONLY AVAILABLE employees or employees on OTHER projects.

Provide top 5 matches in this format:

1. **[Employee Name]** - Match Score: [0-100]
   Current Assignment: [Available / Assigned to: Other Project Name]
   Matching Skills: [Skill1, Skill2, Skill3]
   Missing Skills: [Skill1, Skill2] or None
   Fit: [One sentence why they're a good match]

Rules:
- EXCLUDE any employee whose "Current Project" matches the project description
- Only recommend employees who are Available or on different projects
- Rank by skill match and availability
- Do NOT mention excluded employees
- Do NOT provide alternative recommendations
- Do NOT explain your filtering process
- Just list the top 5 available matches
- Each entry: 3 lines maximum

Answer:
""",
    input_variables=["question", "documents"],
)

training_recommendation_prompt = PromptTemplate(
    template="""You are a career development advisor helping employees grow their skills.
    
    Context: {documents}
    
    Analyze the employee's current skills against company project needs and recommend NEW skills.
    
    Provide your response in this exact format:
    return the employee profile name, employee_id, skills
    ## RECOMMENDED SKILLS
    
    1. [Skill Name] - Priority: [High/Medium/Low]
       Why: [One sentence explaining which active project needs this skill]
    
    2. [Skill Name] - Priority: [High/Medium/Low]
       Why: [One sentence explaining which active project needs this skill]
    
    (Continue for 1-5 skills)
    
    ## RECOMMENDED TRAINING PROGRAMS
    
    1. [Training Name]
       Teaches: [Skill Name]
       Benefit: [One sentence on how this helps with company projects]
    
    2. [Training Name]
       Teaches: [Skill Name]
       Benefit: [One sentence on how this helps with company projects]
    
    (Continue for 1-5 trainings)
    
    Important:
    - Only suggest skills the employee does NOT currently have
    - Reference actual project names from the context
    - Prioritize skills needed for active or upcoming projects
    - Keep each explanation to ONE clear sentence
    
    Answer:
    """,
    input_variables=["documents"],
)

# sql_prompt = PromptTemplate(
#     template="""You are an expert SQL assistant. Your task is to generate a valid SQL query based on the user's question and the database schema provided. Use only the information in the schema context below. Ensure your query is syntactically correct, semantically accurate, and limited to SELECT queries only.
#     Guidelines:
#     - Only use tables, columns, and relationships defined in the schema.
#     - Do not invent column or table names. If something is unclear, write a SQL comment.
#     - Use JOINs where necessary to combine data across tables.
#     - Use GROUP BY and aggregate functions when the question implies summarization.
#     - Always use LIMIT in queries requesting a preview or top-N results.
#     - Format queries cleanly with appropriate indentation.
#     - Never write DELETE, INSERT, UPDATE, DROP, or DDL statements.
#     - Prefer using table aliases (`c` for customers, `o` for orders) when dealing with multiple tables.
#     - All dates must follow 'YYYY-MM-DD'  or 'YYYY-MM-DD HH-mm-ss format.
#
#     Database Schema from YAML:
#     {yaml_context}
#
#     Available Tables:
#     {table_info}
#
#     Question: {input}
#     SQL Query:
#     """,
#     input_variables=["input", "table_info", "yaml_context"],
# )
#
# sql_answer_prompt = PromptTemplate(
#     template="""Given the following user question, corresponding SQL query, and SQL result, answer the user question.
#
#     Question: {question}
#     SQL Query: {query}
#     SQL Result: {result}
#     Answer:
#     """,
#     input_variables=["question", "query", "result"],
# )



chroma_client = chromadb.PersistentClient(path=persist_directory, settings=CHROMA_SETTINGS)
