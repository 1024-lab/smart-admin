
/**
 *
 */
export interface ResponseModel<T> {
    /**
     *
     * @type {number}
     * @memberof ResponseDtoOfLoginDetailVo
     */
    code: number;
    /**
     *
     * @type {LoginDetailVo}
     * @memberof ResponseDtoOfLoginDetailVo
     */
    data: T;
    /**
     *
     * @type {string}
     * @memberof ResponseDtoOfLoginDetailVo
     */
    msg?: string;
    /**
     *
     * @type {boolean}
     * @memberof ResponseDtoOfLoginDetailVo
     */
    success: boolean;
}