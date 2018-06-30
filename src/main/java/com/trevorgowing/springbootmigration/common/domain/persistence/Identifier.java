package com.trevorgowing.springbootmigration.common.domain.persistence;

import com.trevorgowing.springbootmigration.common.domain.constant.IdentifierUse;
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
public abstract class Identifier implements Serializable {

  private static final long serialVersionUID = 5919231994260255805L;

  protected String system;
  protected String value;
  protected IdentifierUse use;

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Identifier otherIdentifier = (Identifier) other;
    return Objects.equals(getSystem(), otherIdentifier.getSystem())
        && Objects.equals(getValue(), otherIdentifier.getValue())
        && getUse() == otherIdentifier.getUse();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSystem(), getValue(), getUse());
  }
}
