package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> savedLines = lineRepository.findAll();
        return savedLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findByLineId(Long id) {
        Line savedLine = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 라인은 존재하지 않습니다."));

        return LineResponse.of(savedLine);
    }

    public LineResponse updateByLineId(Long id, LineRequest lineRequest) {
        Line savedLine = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 라인은 존재하지 않습니다."));

        savedLine.updateName(lineRequest.getName());
        savedLine.updateColor(lineRequest.getColor());

        return LineResponse.of(savedLine);
    }

    public void deleteByLineId(Long id) {
        Line savedLine = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 라인은 존재하지 않습니다."));

        lineRepository.delete(savedLine);
    }
}
