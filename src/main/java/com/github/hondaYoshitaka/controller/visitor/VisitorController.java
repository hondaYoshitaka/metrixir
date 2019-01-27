package com.github.hondaYoshitaka.controller.visitor;

import com.github.hondaYoshitaka.model.form.visitor.VisitorCreateForm;
import com.github.hondaYoshitaka.model.response.visitor.VisitorCreateResponse;
import com.github.hondaYoshitaka.service.visitor.VisitorService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(final VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping("visitors")
    @ResponseStatus(code = HttpStatus.CREATED)
    public VisitorCreateResponse create(
            @RequestBody @Validated final VisitorCreateForm form
    ) {
        return visitorService.create(form.getHostId());
    }
}
