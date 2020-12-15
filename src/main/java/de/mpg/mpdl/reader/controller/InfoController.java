package de.mpg.mpdl.reader.controller;

import de.mpg.mpdl.reader.common.BaseResponseDTO;
import de.mpg.mpdl.reader.common.CommonUtils;
import de.mpg.mpdl.reader.common.ResponseBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shidenghui@gmail.com
 * @date 2020/12/15
 */
@RestController
@RequestMapping("/rest/info")
public class InfoController {

    @Value("${app.latest.version}")
    private String latestVersion;

    @GetMapping(value = "/version")
    public BaseResponseDTO<Boolean> checkVersion(@RequestHeader(name = "X-Version") String version) {
        return ResponseBuilder.buildSuccess(CommonUtils.needUpdate(version, latestVersion));
    }


}
