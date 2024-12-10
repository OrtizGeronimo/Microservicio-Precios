package precio.precios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import precio.precios.exception.ValidationException;
import precio.precios.model.Deal;
import precio.precios.model.Price;
import precio.precios.model.dto.ApplyDealDTO;
import precio.precios.service.PriceService;

@RestController
@RequestMapping("/v1/price")
public class PriceController {

    @Autowired
    private PriceService service;

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Price price){
        service.update(price);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createDeal")
    public ResponseEntity<?> createDeal(@RequestBody Deal deal) throws ValidationException {
        service.createDeal(deal);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/applyDeal")
    public ResponseEntity<?> applyDeal(@RequestBody ApplyDealDTO dto) throws ValidationException {
        service.applyDeal(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getPrice(@PathVariable Long articleId){
        return ResponseEntity.ok().body(service.getPrice(articleId));
    }

    @GetMapping("/{articleId}/history")
    public ResponseEntity<?> getPriceHistory(@PathVariable Long articleId){
        return ResponseEntity.ok().body(service.getPriceHistory(articleId));
    }

    @PostMapping("")
    public ResponseEntity<?> getPriceWithDeal(@RequestBody ApplyDealDTO dto) throws ValidationException {
        return ResponseEntity.ok().body(service.getPriceWithDeal(dto));
    }

}
