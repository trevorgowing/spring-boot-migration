package com.trevorgowing.springbootmigration.patient;

import static lombok.AccessLevel.PRIVATE;

import com.trevorgowing.springbootmigration.common.domain.constant.Gender;
import com.trevorgowing.springbootmigration.common.domain.persistence.HumanName;
import com.trevorgowing.springbootmigration.common.domain.persistence.Identifier;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@Document(collection = "patients")
@AllArgsConstructor(access = PRIVATE)
class Patient implements Serializable {

  private static final long serialVersionUID = 4201950435560110314L;

  @Id private String id;
  private Gender gender;
  private boolean active;
  private HumanName name;
  private LocalDate birthDate;
  @Builder.Default private Set<Identifier> identifiers = new HashSet<>();
}
