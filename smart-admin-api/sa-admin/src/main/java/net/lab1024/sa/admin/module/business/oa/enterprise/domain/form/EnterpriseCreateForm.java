package net.lab1024.sa.admin.module.business.oa.enterprise.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.oa.enterprise.constant.EnterpriseTypeEnum;
import net.lab1024.sa.base.common.json.deserializer.FileKeyVoDeserializer;
import net.lab1024.sa.base.common.json.serializer.FileKeyVoSerializer;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.util.SmartVerificationUtil;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * OA企业模块创建
 *
 * @Author 1024创新实验室: 开云
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EnterpriseCreateForm {

    @Schema(description = "企业名称")
    @NotBlank(message = "企业名称不能为空")
    @Length(max = 200, message = "企业名称最多200字符")
    private String enterpriseName;

    @Schema(description = "企业logo")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String enterpriseLogo;

    @Schema(description = "统一社会信用代码")
    @NotBlank(message = "统一社会信用代码不能为空")
    @Length(max = 200, message = "统一社会信用代码最多200字符")
    private String unifiedSocialCreditCode;

    @Schema(description = "联系人")
    @NotBlank(message = "联系人不能为空")
    @Length(max = 100, message = "联系人最多100字符")
    private String contact;

    @Schema(description = "联系人电话")
    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式不正确")
    private String contactPhone;

    @SchemaEnum(desc = "类型", value = EnterpriseTypeEnum.class)
    @CheckEnum(message = "类型不正确", value = EnterpriseTypeEnum.class)
    private Integer type;

    @Schema(description = "邮箱")
    @Pattern(regexp = SmartVerificationUtil.EMAIL, message = "邮箱格式不正确")
    private String email;

    @Schema(description = "省份")
    private Integer province;

    @Schema(description = "省份名称")
    private String provinceName;

    @Schema(description = "城市")
    private Integer city;

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(description = "区县")
    private Integer district;

    @Schema(description = "区县名称")
    private String districtName;

    @Schema(description = "详细地址")
    @Length(max = 500, message = "详细地址最多500字符")
    private String address;

    @Schema(description = "营业执照")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String businessLicense;

    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @Schema(description = "创建人", hidden = true)
    private String createUserName;

}
