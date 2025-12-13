import os
import ChatBot.Constants as Constants
from dotenv import load_dotenv
import fitz  # PyMuPDF
from langchain_core.documents import Document
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_chroma import Chroma
from sqlalchemy import create_engine
from sqlalchemy.sql import text as text_sql
from sqlalchemy.orm import sessionmaker

if not load_dotenv():
    print("please check that env file exist and readable")
    exit(0)

from ChatBot.Constants import CHROMA_SETTINGS
import chromadb

# environment variables
load_dotenv()
persist_directory = Constants.persist_directory
persist_directory_pdf = os.path.join(persist_directory, "pdf_db")
persist_directory_sql = os.path.join(persist_directory, "sql_db")

embeddings_id = Constants.embeddings_id
embeddings = Constants.embeddings
MODEL = Constants.MODEL
pdf_file_paths = os.getenv("pdf_PATH")


mysql_url = os.getenv('SQL_db')
engine = create_engine(
    mysql_url,
    pool_pre_ping=True,
    pool_recycle=3600
)
Session = sessionmaker(bind=engine)

def load_pdf_mupdf(file_path):
    doc = fitz.open(file_path)
    documents = []

    for page_num in range(len(doc)):
        page = doc.load_page(page_num)
        text = page.get_text()
        if text.strip():
            metadata = {"source": file_path, "page": page_num}
            documents.append(Document(page_content=text, metadata=metadata))

    doc.close()
    return documents


def load_sql_employees():
    session = Session()

    query = text_sql("""
    SELECT 
        e.id, e.username, e.email, e.full_name, e.age, e.gender,
        e.company_id, e.manager_id, e.project_id,
        GROUP_CONCAT(DISTINCT s.name) as skills,
        GROUP_CONCAT(DISTINCT s.id) as skill_ids,
        m.full_name as manager_name,
        p.description as project_description,
        c.name as company_name
    FROM employee e
    LEFT JOIN employee_skills es ON e.id = es.employee_id
    LEFT JOIN skills s ON es.skills_id = s.id
    LEFT JOIN manager m ON e.manager_id = m.id
    LEFT JOIN project p ON e.project_id = p.id
    LEFT JOIN company c ON e.company_id = c.id
    GROUP BY e.id
    """)

    result = session.execute(query)
    employees = [dict(row._mapping) for row in result]
    session.close()

    documents = []
    for emp in employees:
        text = f"""
            Employee Profile:
            Name: {emp['full_name']}
            Username: {emp['username']}
            Email: {emp['email']}
            Age: {emp['age']}
            Gender: {emp['gender']}
            Company: {emp['company_name'] or 'N/A'}
            Manager: {emp['manager_name'] or 'N/A'}
            Current Skills: {emp['skills'] or 'None'}
            Current Project: {emp['project_description'] or 'Not assigned'}
            Availability: {'Available' if not emp['project_id'] else 'Assigned to project'}
            """

        metadata = {
            "type": "employee",
            "id": emp['id'],
            "company_id": emp['company_id'],
            "manager_id": emp['manager_id'] if emp['manager_id'] else None,
            "project_id": emp['project_id'] if emp['project_id'] else None,
            "available": emp['project_id'] is None,
            "skill_ids": emp['skill_ids'] or "",
            "username": emp['username'],
            "email": emp['email']
        }

        documents.append(Document(page_content=text, metadata=metadata))

    return documents


def load_sql_projects():
    session = Session()

    query = text_sql("""
            SELECT 
                p.id, p.description, p.status, p.risk, p.start_date, p.end_date,
                p.company_id,
                GROUP_CONCAT(DISTINCT s.name) as required_skills,
                GROUP_CONCAT(DISTINCT s.id) as required_skill_ids,
                c.name as company_name
            FROM project p
            LEFT JOIN project_skills ps ON p.id = ps.projects_id
            LEFT JOIN skills s ON ps.skills_id = s.id
            LEFT JOIN company c ON p.company_id = c.id
            GROUP BY p.id
            """)

    result = session.execute(query)
    projects = [dict(row._mapping) for row in result]
    session.close()

    documents = []
    for proj in projects:
        text = f"""
            Project Details:
            Description: {proj['description']}
            Status: {proj['status']}
            Risk Level: {proj['risk']}
            Company: {proj['company_name'] or 'N/A'}
            Required Skills: {proj['required_skills'] or 'None specified'}
            Start Date: {proj['start_date']}
            End Date: {proj['end_date'] or 'Ongoing'}
            """

        metadata = {
            "type": "project",
            "id": proj['id'],
            "company_id": proj['company_id'],
            "status": proj['status'],
            "risk": proj['risk'],
            "required_skill_ids": proj['required_skill_ids'] or "",
            "start_date": str(proj['start_date']),
            "end_date": str(proj['end_date']) if proj['end_date'] else "",
            "active": proj['status'] in ['in_progress', 'approved', 'pending']
        }

        documents.append(Document(page_content=text, metadata=metadata))

    return documents


def load_sql_skills():
    session = Session()

    query = text_sql("""
            SELECT 
                s.id, s.name, s.description, s.company_id,
                c.name as company_name
            FROM skills s
            LEFT JOIN company c ON s.company_id = c.id
            """)

    result = session.execute(query)
    skills = [dict(row._mapping) for row in result]
    session.close()

    documents = []
    for skill in skills:
        text = f"""
            Skill Information:
            Name: {skill['name']}
            Description: {skill['description']}
            Company: {skill['company_name'] or 'N/A'}
            """

        metadata = {
            "type": "skill",
            "id": skill['id'],
            "company_id": skill['company_id'] if skill['company_id'] else None,
            "name": skill['name']
        }

        documents.append(Document(page_content=text, metadata=metadata))

    return documents


def load_sql_trainings():
    session = Session()

    query = text_sql("""
    SELECT 
        t.id, t.name, t.description, t.company_id, t.skills_id,
        s.name as skill_name,
        c.name as company_name
    FROM training t
    LEFT JOIN skills s ON t.skills_id = s.id
    LEFT JOIN company c ON t.company_id = c.id
    """)

    result = session.execute(query)
    trainings = [dict(row._mapping) for row in result]
    session.close()

    documents = []
    for training in trainings:
        text = f"""
            Training Program:
            Name: {training['name']}
            Description: {training['description']}
            Related Skill: {training['skill_name'] or 'N/A'}
            Company: {training['company_name'] or 'N/A'}
            """

        metadata = {
            "type": "training",
            "id": training['id'],
            "company_id": training['company_id'] if training['company_id'] else None,
            "skill_id": training['skills_id'] if training['skills_id'] else None,
            "name": training['name']
        }

        documents.append(Document(page_content=text, metadata=metadata))

    return documents


def load_sql_data():
    documents = []

    print("Loading employees...")
    documents.extend(load_sql_employees())

    print("Loading projects...")
    documents.extend(load_sql_projects())

    print("Loading skills...")
    documents.extend(load_sql_skills())

    print("Loading trainings...")
    documents.extend(load_sql_trainings())

    return documents


def split_documents(documents):
    splitter = RecursiveCharacterTextSplitter(chunk_size=Constants.chunk_size, chunk_overlap=Constants.chunk_overlap)
    return splitter.split_documents(documents)


def create_or_get_chroma_client(persist_dir):
    os.makedirs(persist_dir, exist_ok=True)
    client = chromadb.PersistentClient(path=persist_dir, settings=CHROMA_SETTINGS)
    try:
        collection = client.get_collection("documents")
    except Exception:
        collection = client.create_collection("documents")
    return client, collection


def does_vectorstore_exist(persist_dir, embeddings, chroma_client):
    try:
        db = Chroma(persist_directory=persist_dir, embedding_function=embeddings, client_settings=CHROMA_SETTINGS,
                    client=chroma_client)
    except Exception:
        return False
    if not db.get()['documents']:
        return False
    return True


def ingest_files_to_chroma(file_paths, file_type='pdf'):
    all_documents = []

    if file_type == 'pdf':
        loader = load_pdf_mupdf
        persist_dir = persist_directory_pdf
    else:
        loader = load_sql_data
        persist_dir = persist_directory_sql

    if file_type == 'sql':
        all_documents = loader()
    else:
        for path in file_paths:
            docs = loader(path)
            all_documents.extend(docs)


    if file_type != 'sql':
        chunks = split_documents(all_documents)
    else:
        chunks = all_documents

    chroma_client, _ = create_or_get_chroma_client(persist_dir)

    # create or load Chroma vectorstore
    if does_vectorstore_exist(persist_dir, embeddings, chroma_client):
        print(f"appending to existing vectorstore at {persist_dir}")
        db = Chroma(persist_directory=persist_dir, embedding_function=embeddings, client_settings=CHROMA_SETTINGS,
                    client=chroma_client)
        db.add_documents(chunks)
    else:
        print(f"creating new vectorstore at {persist_dir}")
        db = Chroma.from_documents(documents=chunks, embedding=embeddings, persist_directory=persist_dir,
                                   client_settings=CHROMA_SETTINGS, client=chroma_client)

    print(f"ingestion complete\ttotal chunks: {len(chunks)}")
    return db


def get_vectorstore(db_type='pdf'):
    if db_type == 'pdf':
        persist_dir = persist_directory_pdf
    else:
        persist_dir = persist_directory_sql

    chroma_client = chromadb.PersistentClient(settings=CHROMA_SETTINGS, path=persist_dir)
    if does_vectorstore_exist(persist_dir, embeddings, chroma_client):
        return True, Chroma(persist_directory=persist_dir, embedding_function=embeddings,
                            client_settings=CHROMA_SETTINGS,
                            client=chroma_client)
    return False, None

#
# ingest_files_to_chroma([pdf_file_paths], file_type='pdf')
# exists, db = get_vectorstore(db_type='pdf')
#
# ingest_files_to_chroma([], file_type='sql')
# exists2, db2 = get_vectorstore(db_type='sql')