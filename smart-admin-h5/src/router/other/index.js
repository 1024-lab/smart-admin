/**
 * 其他路由
 */

import { user } from './user';
import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';
import { develop } from '@/router/other/develop';
import { school } from '@/router/other/school';
import { contactCompany } from '@/router/other/erp/contact-company';

export const otherRouter = [
  {
    path: '/other-main',
    meta: {
      keepAlive: true,
      permissionType: ROUTER_PERMISSION_TYPE.NO_VALID.value
    },
    component: () => import('views/main/other-main'),
    children: [
      ...user,
      ...develop,
      ...school,
      ...contactCompany
    ]
  }];
