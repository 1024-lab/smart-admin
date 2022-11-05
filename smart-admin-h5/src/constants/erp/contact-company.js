/**
 * 往来单位性质
 * @type {{ENTERPRISE: {value: number, desc: string}, PERSONAL: {value: number, desc: string}}}
 */
export const CONTACT_COMPANY_NATURE_ENUM =
  {
    ENTERPRISE: {
      value: 0,
      desc: '企业'
    },
    PERSONAL: {
      value: 1,
      desc: '个人'
    }
  };
/**
 * 往来机构类型
 * @type {{CUSTOMER: {value: number, desc: string}, SUPPLIER: {value: number, desc: string}}}
 */
export const
  CONTACT_COMPANY_TYPE_ENUM =
    {
      CUSTOMER: {
        value: 0,
        desc: '客户'
      },
      SUPPLIER: {
        value: 1,
        desc: '供应商'
      },
      SCHOOL: {
        value: 2,
        desc: '分校'
      },
      COOPERATIVE_ORG: {
        value: 3,
        desc: '合作机构'
      }
    };

/**
 * 付款方式
 * @type {{BANK: {value: number, desc: string}, ZHI_FU_BAO: {value: number, desc: string}, WE_CHAT: {value: number, desc: string}}}
 */
export const
  PAYMENT_TYPE_ENUM =
    {
      BANK: {
        value: 0,
        desc: '银行卡'
      },
      WE_CHAT: {
        value: 1,
        desc: '微信'
      },
      ZHI_FU_BAO: {
        value: 2,
        desc: '支付宝'
      }
    };

/**
 * 往来机构余额类型
 * @type {{RECEIVE_BALANCE: {value: number, desc: string}, PAY_BALANCE: {value: number, desc: string}}}
 */
export const
  CONTACT_COMPANY_BALANCE_TYPE =
    {
      PAY_BALANCE: {
        value: 0,
        desc: '应付款余额'
      },
      RECEIVE_BALANCE: {
        value: 1,
        desc: '应收款余额'
      }
    };

/**
 * 往来单位等级
 * @type {{CORE: {value: number, desc: string}, POTENTIAL: {value: number, desc: string}, BAD: {value: number, desc: string}, GENERAL: {value: number, desc: string}}}
 */
export const CONTACT_COMPANY_GRADE_ENUM = {
  CORE: {
    value: 1,
    desc: '核心',
    color: 'green'
  },
  POTENTIAL: {
    value: 2,
    desc: '有潜力',
    color: 'cyan'
  },
  GENERAL: {
    value: 3,
    desc: '普通',
    color: 'blue'
  },
  BAD: {
    value: 4,
    desc: '较差',
    color: 'purple'
  }
};

/**
 * 往来单位等级
 * @type {{OWNER: {value: number, desc: string}, SHARER: {value: number, desc: string}, COMMON: {value: number, desc: string}}}
 */
export const CONTACT_COMPANY_SHARE_TYPE_ENUM = {
  OWNER: {
    value: 0,
    desc: '属于我的'
  },
  SHARER: {
    value: 1,
    desc: '共享的'
  },
  COMMON: {
    value: 2,
    desc: '公共的'
  }
};

/**
 * 往来单位标签
 * @type {{OWNER: {value: number, desc: string}, SHARER: {value: number, desc: string}, COMMON: {value: number, desc: string}}}
 */
export const CONTACT_COMPANY_TAG_ENUM = {
  POTENTIAL: {
    value: 0,
    desc: '潜在',
    color: 'green'
  },
  INTENTION: {
    value: 1,
    desc: '意向',
    color: 'cyan'
  },
  NEGOTIATION: {
    value: 2,
    desc: '洽谈',
    color: 'blue'
  },
  DEAL: {
    value: 3,
    desc: '成交',
    color: 'geekblue'
  },
  LOSS: {
    value: 4,
    desc: '流失',
    color: 'red'
  }
};

export default {
  CONTACT_COMPANY_NATURE_ENUM,
  CONTACT_COMPANY_TYPE_ENUM,
  PAYMENT_TYPE_ENUM,
  CONTACT_COMPANY_BALANCE_TYPE,
  CONTACT_COMPANY_GRADE_ENUM,
  CONTACT_COMPANY_SHARE_TYPE_ENUM,
  CONTACT_COMPANY_TAG_ENUM
};
