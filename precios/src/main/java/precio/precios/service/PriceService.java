package precio.precios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import precio.precios.exception.ValidationException;
import precio.precios.model.Deal;
import precio.precios.model.Price;
import precio.precios.model.dto.*;
import precio.precios.repo.DealRepo;
import precio.precios.repo.PriceRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceService {

    @Autowired
    private PriceRepo repo;

    @Autowired
    private DealRepo dealRepo;

    public void update(Price newPrice) {
        //todo validaciones al precio
        Optional<Price> priceOpt = repo.findByArticleId(newPrice.getArticleId());

        if (priceOpt.isPresent()){
            Price price = priceOpt.get();
            price.setEffectiveDate(LocalDate.now());

            repo.save(price);

            newPrice.setDeals(price.getDeals());
        }

        repo.save(newPrice);
    }

    public void createDeal(Deal deal) throws ValidationException {
        //todo validaciones de fechas y monto

        dealRepo.findDealByCode(deal.getCode()).orElseThrow(() -> new ValidationException("Ya existe una oferta con el código ingresado"));

        dealRepo.save(deal);
    }

    public void applyDeal(ApplyDealDTO dto) throws ValidationException {

        Deal deal = dealRepo.findById(dto.getDealId()).orElseThrow(() -> new ValidationException("No existe una oferta con el id indicado"));

        Price price = repo.findByArticleId(dto.getArticleId()).get();

        price.getDeals().add(deal);

        repo.save(price);

    }

    public PriceDTO getPrice(Long articleId) {

        Price price = repo.findByArticleId(articleId).get(); //todo si pasa la validacion de article exist, entonces tiene que tener precio

        PriceDTO response = new PriceDTO();

        response.setAmount(price.getPrice());
        response.setArticleId(articleId);
        return response;
    }

    public PriceHistoryDTO getPriceHistory(Long articleId) {

        List<Price> prices = repo.findAllByArticleId(articleId);

        PriceHistoryDTO response = new PriceHistoryDTO();

        response.setArticleId(articleId);
        response.setPrices(prices.stream().map(price -> {
            HistoryDetailDTO dto = new HistoryDetailDTO();
            dto.setAmount(price.getPrice());
            dto.setEffectiveDate(price.getEffectiveDate());
            return dto;
        }).collect(Collectors.toList()));

        return response;
    }


    public PriceWithDealDTO getPriceWithDeal(ApplyDealDTO dto) throws ValidationException {

        Deal deal = dealRepo.findById(dto.getDealId()).orElseThrow(() -> new ValidationException("No existe una oferta con el id indicado"));

        Price price = repo.findByArticleId(dto.getArticleId()).get();

        PriceWithDealDTO response = new PriceWithDealDTO();

        response.setDealId(dto.getDealId());
        response.setArticleId(dto.getArticleId());
        response.setPrice(price.getPrice());
        response.setFinalPrice(price.getPrice() + deal.getAmount());

        return response;
    }
}
