package net.lab1024.smartadmin.module.support.idgenerator.constant;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/8 0008 下午 13:43
 * @since JDK1.8
 */
public enum IdGeneratorEnum {


    /**
     * 测试generate
     */
    TEST_ID_GENERATOR(2, "testIdGenerator"),


    ORDER(1, "order");

    private long id;
    private String keyName;

    IdGeneratorEnum(int id, String keyName) {
        this.id = id;
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "IdGeneratorEnum{" + "id=" + id + ", keyName='" + keyName + '\'' + '}';
    }

    public long getId() {
        return id;
    }

    public String getKeyName() {
        return keyName;
    }

}
