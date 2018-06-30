package com.trevorgowing.springbootmigration.common.domain.persistence;

import com.trevorgowing.springbootmigration.common.domain.constant.NameUse;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HumanName implements Serializable {

  private static final long serialVersionUID = 7058493860997640783L;

  private String prefix;
  private String given;
  private String family;
  private String suffix;
  private String text;
  private NameUse use;

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    HumanName otherName = (HumanName) other;
    return Objects.equals(getPrefix(), otherName.getPrefix())
        && Objects.equals(getGiven(), otherName.getGiven())
        && Objects.equals(getFamily(), otherName.getFamily())
        && Objects.equals(getSuffix(), otherName.getSuffix());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPrefix(), getGiven(), getFamily(), getSuffix());
  }
}
