package com.github.nekolr.slime.controller;

import com.github.nekolr.slime.ExpressionEngine;
import com.github.nekolr.slime.domain.SpiderFlow;
import com.github.nekolr.slime.model.Shape;
import com.github.nekolr.slime.io.Line;
import com.github.nekolr.slime.service.SpiderFlowService;
import com.github.nekolr.slime.support.PageRequest;
import com.github.nekolr.slime.util.ExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/flow")
@Slf4j
public class SpiderFlowController {

    @Resource
    private SpiderFlowService spiderFlowService;

    @Resource
    private ExpressionEngine expressionEngine;

    @Resource
    private ExecutorFactory executorFactory;


    @PostMapping(value = "/list")
    public ResponseEntity<Page<SpiderFlow>> list(@RequestParam(name = "page") Integer page, @RequestParam(name = "limit") Integer size, SpiderFlow flow) {
        PageRequest request = new PageRequest(page, size);
        return ResponseEntity.ok(spiderFlowService.findAll(flow, request.toPageable()));
    }

    @RequestMapping("/objects")
    public ResponseEntity expressionObjects() {
        return ResponseEntity.ok(expressionEngine.getExpressionObjectMap());
    }

    @RequestMapping("/other")
    public ResponseEntity<List<SpiderFlow>> otherFlows(Long id) {
        if (Objects.isNull(id)) {
            return ResponseEntity.ok(spiderFlowService.findAll());
        }
        return ResponseEntity.ok(spiderFlowService.findOtherFlows(id));
    }

    @RequestMapping("/cron")
    public ResponseEntity updateCron(Long id, String cron) {
        spiderFlowService.updateCronAndNextExecuteTime(id, cron);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/save")
    public ResponseEntity<Long> save(SpiderFlow flow) {
        spiderFlowService.save(flow);
        return ResponseEntity.ok((flow.getId()));
    }

    @RequestMapping("/xml")
    public ResponseEntity<String> getXml(Long id) {
        return ResponseEntity.ok(spiderFlowService.getById(id).getXml());
    }

    @RequestMapping("/log")
    public ResponseEntity<List<Line>> log(Long id, Long taskId, String keywords, Long index, Integer count, Boolean reversed, Boolean matchCase, Boolean regex) {
        return ResponseEntity.ok(spiderFlowService.log(id, taskId, keywords, index, count, reversed, matchCase, regex));
    }

    @RequestMapping("/remove")
    public ResponseEntity remove(Long id) {
        spiderFlowService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent5TriggerTime")
    public ResponseEntity<List<String>> getRecent5TriggerTime(String cron) {
        return ResponseEntity.ok(spiderFlowService.getRecentTriggerTime(cron, 5));
    }

    @RequestMapping("/shapes")
    public ResponseEntity<List<Shape>> shapes() {
        return ResponseEntity.ok(executorFactory.shapes());
    }

    @RequestMapping("/start")
    public ResponseEntity start(Long id) {
        spiderFlowService.start(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/run")
    public ResponseEntity run(Long id) {
        spiderFlowService.run(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/stop")
    public ResponseEntity stop(Long id) {
        spiderFlowService.stop(id);
        return ResponseEntity.ok().build();
    }

}