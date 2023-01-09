package sigma.internship.petProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.GameDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoundServiceImpl implements RoundService {
    @Override
    public List<GameDto> findAll() {
        return null;
    }

    @Override
    public List<GameDto> findAllByGameSessionId(long id) {
        return null;
    }

    @Override
    public List<GameDto> findAllByUserId(long id) {
        return null;
    }

    @Override
    public List<GameDto> findAllByGameId(long id) {
        return null;
    }

    @Override
    public List<GameDto> findAllByResultType(long id) {
        return null;
    }
}
