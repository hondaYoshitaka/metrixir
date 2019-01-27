package com.github.hondaYoshitaka.controller.host;

import com.github.hondaYoshitaka.model.form.host.HostCreateForm;
import com.github.hondaYoshitaka.model.response.host.HostCreateResponse;
import com.github.hondaYoshitaka.service.host.HostService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class HostController {

    private final HostService hostService;

    public HostController(final HostService hostService) {
        this.hostService = hostService;
    }

    @PostMapping("hosts")
    @ResponseStatus(code = HttpStatus.CREATED)
    public HostCreateResponse create(
            @RequestBody @Validated final HostCreateForm form
    ) {
        return hostService.create(form.getHostName(), form.getTag());
    }
}
