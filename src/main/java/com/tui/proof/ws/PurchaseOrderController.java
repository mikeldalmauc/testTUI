package com.tui.proof.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.DtoMapper;
import com.tui.proof.dto.PurchaseOrderDto;
import com.tui.proof.service.PurchaseOrderService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/orders")
public class PurchaseOrderController {

  @Autowired
  private PurchaseOrderService purchaseOrderService;

  @GetMapping("/")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("Hello, Im Miquel Montoro and you can order pilotes in this service.\n"
        + "You can find more info about the service at : ");
  }

  @PostMapping("/create")
  @ResponseBody
  public ResponseEntity<PurchaseOrderDto> create(@Valid @RequestBody PurchaseOrderDto purchaseOrderDto) {
    log.info("create purchaseOrder {}", purchaseOrderDto);

    return ResponseEntity.ok(
      DtoMapper.convertToDto(
        purchaseOrderService.create(
          DtoMapper.convertToDo(purchaseOrderDto))));
  }

  @PostMapping("/update")
  @ResponseBody
  public ResponseEntity<PurchaseOrderDto> update(@Valid @RequestBody PurchaseOrderDto purchaseOrderDto) {
    log.info("update purchaseOrder {}", purchaseOrderDto);

    if(StringUtils.isBlank(purchaseOrderDto.getNumber())
      || purchaseOrderDto.getId() != null){
      return ResponseEntity.badRequest().body(purchaseOrderDto);
    }

    return ResponseEntity.ok(
      DtoMapper.convertToDto(
        purchaseOrderService.update(
          DtoMapper.convertToDo(purchaseOrderDto))));
    
  }

  @RolesAllowed("ADMIN")
  @ResponseBody
  @GetMapping("/search")
  public ResponseEntity<List<PurchaseOrderDto>> search(@RequestBody ClientDto clientDto) {
    log.info("search purchase orders of client {}", clientDto);

    return ResponseEntity.ok(
      purchaseOrderService.search(
          DtoMapper.convertToDo(clientDto)).stream()
            .map(DtoMapper::convertToDto)
            .collect(Collectors.toList()));
  }

}
