package ga.patrick.smns.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    int id;

    @Column(length = 2000)
    String name;

    /** northeast lat */
    double top;

    /** northeast lon */
    double right;

    /** southwest lat */
    double bottom;

    /** southwest lon */
    double left;

    public Location(String name, double top, double right, double bottom, double left) {
        this.name = name;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }
}
