# SkillFlow

## Table of Contents
- [Introduction](#introduction)
- [Key Features](#key-features)
- [User Roles & Permissions](#user-roles--permissions)
- [Architecture](#architecture)
- [Core Workflows](#core-workflows)
- [Problems Solved](#problems-solved)
- [Contributing](#contributing)

## Introduction

SkillFlow is a comprehensive company-internal skill and project management system with AI-powered recommendations and approval-driven workflows. The project is aimed at project managers and employees where it offers employee improvement through training sessions and project resource allocation through smart AI recommendation of the best fit employees for projects based on skills.

SkillFlow is a centralized backend system built on Java Spring Boot that addresses critical organizational challenges in managing employee capabilities, project assignments, and workload distribution. The platform enforces governance through approval workflows while leveraging AI to optimize resource allocation and employee development.

## Key Features

### Project Management
- Approval-Driven Workflow: Projects progress through create, approve, start, complete stages
- Risk-Based PM Assignment: System validates project manager capacity based on risk levels from low to critical
- Capacity Management: Prevents PM overload with automatic risk load tracking
- AI-Powered Recommendations: Smart employee and skill matching for projects

### Skills Management
- Verified Skill Profiles: Prevents uncontrolled self-reporting through approval workflows
- Two-Tier Skill Addition:
  - Employees add existing skills from catalog with Manager approval required
  - Request new skills not in system with Company Admin approval required
- AI-Generated Responses: Automated polite rejection emails for declined skill requests

### Training Management
- Training Enrollment: Browse and enroll in approved training programs
- Training Requests: Request new training programs subject to approval
- Skills Integration: Completed training strengthens verified skill profiles
- AI Rejection Handling: Automated courteous responses for declined training requests

### AI Capabilities
1. askRAG: General system information retrieval
2. Skill Recommendations: AI suggests relevant skills based on project descriptions
3. Employee Matching: Recommends best-fit employees for projects based on skills and requirements
4. Training Recommendations: Personalized training suggestions based on employee skills and company projects
5. Automated Communications: AI-generated professional rejection messages

## User Roles & Permissions

### Company Admin
- Approve, reject, start, and complete projects
- Assign projects to Project Managers
- Approve new skills requested by employees
- Review and manage new training programs
- Full system authority

### Manager
- Review employee skill addition requests
- Approve or reject skill requests from team members
- Approve or reject training enrollment requests
- Oversee team development and capability growth

### Project Manager
- Assign and unassign employees to and from projects
- Assign and unassign skills to and from projects
- Request AI recommendations for employee assignments
- Request AI recommendations for project skill requirements

### Employee
- Request to add skills from company catalog
- Request new skills not in the system
- Enroll in approved training programs
- Request new training programs
- Participate in assigned projects
- Request AI training recommendations

## Architecture

### Technology Stack
- Backend: Java Spring Boot
- AI Integration: Python-based Chatbot API using FastAPI and Ollama
- Build Tool: Maven
- Language Distribution: 89.3% Java, 10.7% Python

### Project Structure
```
SkillFlow/
├── src/                          # Main Spring Boot application
├── SkillFlowChatbotAPI/         # Python AI service
├── pom.xml                       # Maven dependencies
└── mvnw                          # Maven wrapper
```

## Core Workflows

### Project Lifecycle
1. Create: Project created with description, risk level, and pending status
2. Admin Approval: Company Admin reviews and approves quality initiatives
3. Start Execution: Approved projects transition to in-progress status
4. PM Assignment: System validates PM capacity before assignment
5. Complete: Project completion automatically reduces PM risk load

### Skill Addition Process
1. Employee requests skill addition
2. Manager for existing skills or Admin for new skills reviews request
3. Upon approval, skill is added to employee profile or company catalog
4. Rejections trigger AI-generated courteous email responses

### Training Process
1. Employee browses training catalog and enrolls OR requests new training
2. Manager or Admin reviews request
3. Approval adds training to employee's development path
4. Rejections handled with AI-generated professional responses

## Problems Solved

### Uncontrolled Skill Growth
Problem: Employees self-report unverified skills, creating unreliable data and mismatched assignments  
Solution: Approval-driven skill verification through Manager and Admin workflows

### Poor Project–Skill/Employee Matching
Problem: Projects lack clear skill requirements, resulting in under-qualified teams  
Solution: AI-powered employee and skill recommendations based on project descriptions

### Workload Imbalance
Problem: Project managers become overloaded without capacity limits  
Solution: Risk-based capacity tracking prevents PM assignment when limits are exceeded

### Lack of Governance
Problem: Sensitive operations occur without proper approval chains  
Solution: Multi-tier approval workflows for projects, skills, and training

### Lack of Employee Improvement
Problem: No proper training system for employee development  
Solution: Structured training catalog with enrollment, requests, and AI recommendations

## Contributing

This project is developed by:
- [MuhammadFaleh](https://github.com/MuhammadFaleh)
    1. (employee, manager, skill, add existing skill, add existing training, training session, training, database schema, AI chatbot API with langchain and fastAPI)
- [OsamaAlahmadi-90](https://github.com/OsamaAlahmadi-90)
    1. (Project, Project Manager, company admin, new skill request, presntaion creation, database schema, email with chatbot)
- [mjedmunif](https://github.com/mjedmunif)
    1. (new company request, company creation, admin, new training creation, company admin,database schema, email with chatbot)

- ## License

Apache License Version 2.0, January 2004

---

**Note**: SkillFlow is designed as a company-internal system and requires proper configuration of user roles and permissions before deployment.
