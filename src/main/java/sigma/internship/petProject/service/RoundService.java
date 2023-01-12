package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.GameDto;
import sigma.internship.petProject.dto.RoundDto;

import java.util.List;

public interface RoundService {

    Page<RoundDto> findAll(Pageable pageable);

    List<RoundDto> findAllByGameSessionId(long id);

    List<RoundDto> findAllByUserId(long id);

    List<RoundDto> findAllByGameId(long id);

    List<RoundDto> findAllByResultType(long id);
}
