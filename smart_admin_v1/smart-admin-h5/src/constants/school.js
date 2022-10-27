/**
 *  分校标签
 */
export const SCHOOL_TAG_ENUM = {
  POTENTIAL: {
    value: 0,
    desc: '潜在'
  },
  INTENTION: {
    value: 1,
    desc: '意向'
  },
  NEGOTIATION: {
    value: 2,
    desc: '洽谈'
  },
  DEAL: {
    value: 3,
    desc: '成交'
  },
  LOSS: {
    value: 4,
    desc: '流失'
  }
};

/**
 * 分校等级
 */
export const SCHOOL_GRADE_ENUM = {
  CORE: {
    value: 1,
    desc: '核心'
  },
  POTENTIAL: {
    value: 2,
    desc: '有潜力'
  },
  GENERAL: {
    value: 3,
    desc: '普通'
  },
  BAD: {
    value: 4,
    desc: '较差'
  }
};

/**
 * 共享类型
 */
export const SCHOOL_SHARE_TYPE_ENUM = {
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

export default {
  SCHOOL_TAG_ENUM,
  SCHOOL_GRADE_ENUM,
  SCHOOL_SHARE_TYPE_ENUM
};
