package com.trevorgowing.springbootmigration.patient;

import com.trevorgowing.springbootmigration.common.domain.constant.Gender;
import com.trevorgowing.springbootmigration.common.domain.persistence.HumanName;
import com.trevorgowing.springbootmigration.common.domain.persistence.Identifier;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "patients")
@CompoundIndexes({
  @CompoundIndex(
    name = "name_system_value_uidx",
    unique = true,
    def = "{'identifiers.system' : 1, 'identifiers.name': 1}"
  )
})
class Patient implements Serializable {

  private static final long serialVersionUID = 4201950435560110314L;

  @Id private String id;
  private Gender gender;
  private boolean active;
  private HumanName name;
  private LocalDate birthDate;
  private Set<Identifier> identifiers = new HashSet<>();
}
