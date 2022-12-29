package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.ResultDto;

public interface ResultService {

    Page<ResultDto> findAll(Pageable pageable);
}
