package dat3.book_app.entity.openAI;

import dat3.book_app.entity.Entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OpenAIResult extends Entities {

    @Column(name = "result")
    private String result;

    @Column(name = "book_reference")
    private String bookReference;
}
