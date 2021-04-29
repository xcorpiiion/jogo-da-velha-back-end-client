package br.com.unip.jogodavelha.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class GameDTO extends AbstractIdDTO {

    private Integer buttonNumber;

    private String currentNickname;

    private String myNickname;

    private String opponentNickname;

    private Long myId;

    private Long opponentId;

}
