package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.ResultDto;

import sigma.internship.petProject.mapper.ResultMapper;
import sigma.internship.petProject.repository.ResultRepository;

@Service
@AllArgsConstructor
@Slf4j
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;

    @Override
    public Page<ResultDto> findAll(Pageable pageable) {
        return resultRepository.findAll(pageable).map(resultMapper::resultToResultDto);
    }

}
