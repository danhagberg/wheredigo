package net.digitaltsunami.wheredigo.controller;

import net.digitaltsunami.wheredigo.model.Spend;
import net.digitaltsunami.wheredigo.service.WheredigoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping("/api/v1/wheredigo")
public class WheredigoRestController {
    private final Logger logger = LoggerFactory.getLogger(WheredigoRestController.class);

    private final net.digitaltsunami.wheredigo.service.WheredigoService spendService;

    @Autowired
    public WheredigoRestController(WheredigoService spendService) {
        this.spendService = spendService;
    }

    @PostMapping()
    public ResponseEntity<Spend> recordSpend(@RequestBody Spend spend) {
        Spend newSpend = spendService.recordSpend(spend);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSpend.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(newSpend);
    }

    @GetMapping()
    public Iterable<Spend> getSpendQuery(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "subcategory", required = false) String subcategory,
            @RequestParam(value = "vendor", required = false) String vendor,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "note", required = false) String note) {
        boolean emptyQuery = isEmpty(category)
                && isEmpty(subcategory)
                && isEmpty(vendor)
                && isEmpty(tag)
                && isEmpty(note);

        if (emptyQuery) {
            return spendService.findAll();
        } else {
            return spendService.findAllByFilter(category, subcategory, vendor, note, tag);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spend> getSpend(@PathVariable String id) {
        Spend spend = spendService.findById(id);
        return ResponseEntity
                .ok()
                .body(spend);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSpend(@PathVariable String id) {
        spendService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spend> updateSpend(@PathVariable String id, @RequestBody Spend spend) {
        spend.setId(id);
        spendService.updateSpend(spend);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(spend.getId())
                .toUri();
        return ResponseEntity
                .ok()
                .body(spend);
    }
}