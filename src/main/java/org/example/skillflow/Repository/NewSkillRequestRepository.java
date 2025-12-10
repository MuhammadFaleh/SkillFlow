package org.example.skillflow.Repository;


import org.example.skillflow.Model.NewSkillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewSkillRequestRepository extends JpaRepository<NewSkillRequest,Integer> {
    NewSkillRequest findNewSkillRequestById(Integer id);
}
