import { theme } from 'ant-design-vue/lib';
import convertLegacyToken from 'ant-design-vue/lib/theme/convertLegacyToken';

const { defaultAlgorithm, defaultSeed } = theme;

const mapToken = defaultAlgorithm(defaultSeed);
const token = convertLegacyToken.default(mapToken);

export default {
  '@primary-color': token['primary-color'], // 全局主色
  '@base-bg-color': '#fff',
  '@hover-bg-color': 'rgba(0, 0, 0, 0.025)',
  '@hover-bg-color-night': 'rgba(255, 255, 255, 0.025)',
  '@header-light-bg-hover-color': '#f6f6f6',
  '@header-height': '80px',
  '@header-user-height': '40px',
  '@page-tag-height': '40px',
  '@theme-list': ['light', 'dark', 'night'],
};
