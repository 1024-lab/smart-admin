package net.lab1024.smartadmin.module.system.login;

import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.module.system.employee.EmployeeService;
import net.lab1024.smartadmin.module.system.employee.constant.EmployeeStatusEnum;
import net.lab1024.smartadmin.module.system.employee.domain.bo.EmployeeBO;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Slf4j
@Service
public class LoginTokenService {

    /**
     * 过期时间一天
     */
    private static final int EXPIRE_SECONDS = 1 * 24 * 3600;
    /**
     * jwt加密字段
     */
    private static final String CLAIM_ID_KEY = "id";

    @Value("${jwt.key}")
    private String jwtKey;

    @Autowired
    private EmployeeService employeeService;


    /**
     * 功能描述: 生成JWT TOKEN
     *
     * @param employeeDTO
     * @return
     * @auther yandanyang
     * @date 2018/9/12 0012 上午 10:08
     */
    public String generateToken(EmployeeDTO employeeDTO) {
        Long id = employeeDTO.getId();
        /**将token设置为jwt格式*/
        String baseToken = UUID.randomUUID().toString();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeExpire = localDateTimeNow.plusSeconds(EXPIRE_SECONDS);
        Date from = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        Date expire = Date.from(localDateTimeExpire.atZone(ZoneId.systemDefault()).toInstant());

        Claims jwtClaims = Jwts.claims().setSubject(baseToken);
        jwtClaims.put(CLAIM_ID_KEY, id);
        String compactJws = Jwts.builder().setClaims(jwtClaims).setNotBefore(from).setExpiration(expire).signWith(SignatureAlgorithm.HS512, jwtKey).compact();

        EmployeeBO employeeBO = employeeService.getById(id);
        RequestTokenBO tokenBO = new RequestTokenBO(employeeBO);

        return compactJws;
    }

    /**
     * 功能描述: 根据登陆token获取登陆信息
     *
     * @param
     * @return
     * @auther yandanyang
     * @date 2018/9/12 0012 上午 10:11
     */
    public RequestTokenBO getEmployeeTokenInfo(String token) {
        Long employeeId = -1L;
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
            String idStr = claims.get(CLAIM_ID_KEY).toString();
            employeeId = Long.valueOf(idStr);
        } catch (Exception e) {
            log.error("getEmployeeTokenInfo error:{}", e);
            return null;
        }

        EmployeeBO employeeBO = employeeService.getById(employeeId);
        if (employeeBO == null) {
            return null;
        }

        if (EmployeeStatusEnum.DISABLED.getValue().equals(employeeBO.getIsDisabled())) {
            return null;
        }

        if (JudgeEnum.YES.equals(employeeBO.getIsLeave())) {
            return null;
        }

        if (JudgeEnum.YES.equals(employeeBO.getIsDelete())) {
            return null;
        }

        return new RequestTokenBO(employeeBO);
    }

}
