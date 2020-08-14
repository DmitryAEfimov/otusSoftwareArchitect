package ru.otus.softwarearchitect.defimov.discoverymock.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.discoverymock.model.DiscoveryReportItem;
import ru.otus.softwarearchitect.defimov.discoverymock.model.ReportRepository;
import ru.otus.softwarearchitect.defimov.discoverymock.service.DataGeneratorService;

import java.util.UUID;

@RestController
@Timed(value = "report_request",
		histogram = true)
public class DiscoveryReportRestController {
	private final ReportRepository reportRepository;
	private final DataGeneratorService dataGeneratorService;

	public DiscoveryReportRestController(ReportRepository reportRepository,
			DataGeneratorService dataGeneratorService) {
		this.reportRepository = reportRepository;
		this.dataGeneratorService = dataGeneratorService;
	}

	@GetMapping(value = "init")
	public void init(@RequestParam(name = "count") int dataCnt) {
		dataGeneratorService.generate(dataCnt);
	}

	@GetMapping(value = "data/{report_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<DiscoveryReportItem> getReportChunk(@PathVariable(name = "report_id") UUID report_id,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "size", required = false, defaultValue = "100") int chunk) {

		return reportRepository.findAllByReportId(report_id, PageRequest.of(pageNum, chunk));
	}
}
