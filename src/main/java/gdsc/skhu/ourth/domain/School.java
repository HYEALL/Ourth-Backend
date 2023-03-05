package gdsc.skhu.ourth.domain;

import gdsc.skhu.ourth.domain.dto.SchoolDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;

    @OneToMany(mappedBy = "school")
    private List<User> users = new ArrayList<>();

    public SchoolDTO.Response toResponseDTO() {
        return SchoolDTO.Response.builder()
                .id(id)
                .schoolName(schoolName)
                .build();
    }

}
