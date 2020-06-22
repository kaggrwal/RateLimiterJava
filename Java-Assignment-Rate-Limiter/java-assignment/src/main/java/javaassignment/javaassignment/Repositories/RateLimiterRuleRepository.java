package javaassignment.javaassignment.Repositories;

import java.util.List;
import javaassignment.javaassignment.Models.RateLimiterRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "rateLimiterRules", path = "rateLimiterRule")
public interface RateLimiterRuleRepository extends JpaRepository<RateLimiterRule,Long> {

  List<RateLimiterRule> findAllByIsActive(boolean flag);

}
