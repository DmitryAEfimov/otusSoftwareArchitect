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
import ru.otus.softwarearchitect.defimov.discoverymock.service.QueueProducerService;
import ru.otus.softwarearchitect.defimov.discoverymock.service.dto.NotificationMessage;

import java.util.UUID;

@RestController
@Timed(value = "report_request",
		histogram = true)
public class DiscoveryReportRestController {
	private final ReportRepository reportRepository;
	private final DataGeneratorService dataGeneratorService;
	private final QueueProducerService queueService;

	public DiscoveryReportRestController(ReportRepository reportRepository,
			DataGeneratorService dataGeneratorService, QueueProducerService queueService) {
		this.reportRepository = reportRepository;
		this.dataGeneratorService = dataGeneratorService;
		this.queueService = queueService;
	}

	@GetMapping(value = "init")
	public NotificationMessage init(@RequestParam(name = "count") int dataCnt) {
		UUID reportId = dataGeneratorService.generate(dataCnt);
		int generatedCnt = reportRepository.countItemsByReportId(reportId);
		NotificationMessage message = new NotificationMessage(reportId, generatedCnt);
		queueService.fireInitializationComplete(message);

		return message;
	}

	@GetMapping(value = "data/{report_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<DiscoveryReportItem> getReportChunk(@PathVariable(name = "report_id") UUID report_id,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "size", required = false, defaultValue = "100") int chunk) {

		return reportRepository.findAllByReportId(report_id, PageRequest.of(pageNum, chunk));
	}
}
