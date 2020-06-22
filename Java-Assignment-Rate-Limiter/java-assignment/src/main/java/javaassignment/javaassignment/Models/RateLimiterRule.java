package javaassignment.javaassignment.Models;


import java.io.Serializable;
import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="rate_limiter_rules")
public class RateLimiterRule implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String userId;

  @Enumerated(EnumType.STRING)
  private ApisIdentifier api;

  private int rateLimit;

  private boolean isActive;

  @Enumerated(EnumType.STRING)
  private RateLimiterTimeUnit rateLimiterTimeUnit;

}
