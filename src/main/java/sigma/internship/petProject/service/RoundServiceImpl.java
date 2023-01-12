package sigma.internship.petProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sigma.internship.petProject.dto.RoundDto;
import sigma.internship.petProject.mapper.RoundMapper;
import sigma.internship.petProject.repository.RoundRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoundServiceImpl implements RoundService {

    private final RoundRepository roundRepository;

    private final RoundMapper roundMapper;

    @Override
    public Page<RoundDto> findAll(Pageable pageable) {
        return roundRepository
                .findAll(pageable)
                .map(roundMapper::roundToRoundDto);
    }

    @Override
    public List<RoundDto> findAllByGameSessionId(long id) {
        return roundRepository
                .findByGameSession(id)
                .stream()
                .map(roundMapper::roundToRoundDto)
                .toList();
    }

    @Override
    public List<RoundDto> findAllByUserId(long id) {
        return null;
    }

    @Override
    public List<RoundDto> findAllByGameId(long id) {
        return null;
    }

    @Override
    public List<RoundDto> findAllByResultType(long id) {
        return null;
    }
}
