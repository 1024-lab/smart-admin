module.exports = {
    root: true,
    env: {
        browser: true,
        es2021: true,
        node: true,
    },
    parser: 'vue-eslint-parser',
    parserOptions: {
        ecmaVersion: 12,
        parser: '@typescript-eslint/parser',
        sourceType: 'module',
    },
    extends: [
        'plugin:vue/vue3-essential',
        'eslint:recommended',
        'plugin:vue/base'
    ],
    globals: {
        defineProps: "readonly",
        defineEmits: "readonly",
        defineExpose: "readonly",
        withDefaults: "readonly"
    },
    plugins: ['vue', '@typescript-eslint'],
    rules: {
        '@typescript-eslint/ban-ts-ignore': 'off',
        '@typescript-eslint/explicit-function-return-type': 'off',
        '@typescript-eslint/no-explicit-any': 'off',
        '@typescript-eslint/no-var-requires': 'off',
        '@typescript-eslint/no-empty-function': 'off',
        'vue/custom-event-name-casing': 'off',
        'no-use-before-define': 'off',
        '@typescript-eslint/no-use-before-define': 'off',
        '@typescript-eslint/ban-ts-comment': 'off',
        '@typescript-eslint/ban-types': 'off',
        '@typescript-eslint/no-non-null-assertion': 'off',
        '@typescript-eslint/explicit-module-boundary-types': 'off',
        '@typescript-eslint/no-unused-vars': [
            'error',
            // we are only using this rule to check for unused arguments since TS
            // catches unused variables but not args.
            { varsIgnorePattern: '.*', args: 'none' }
        ],
        'no-unused-vars': [
            'error',
            // we are only using this rule to check for unused arguments since TS
            // catches unused variables but not args.
            { varsIgnorePattern: '.*', args: 'none' }
        ],
        'space-before-function-paren': 'off',

        'vue/attributes-order': 'off',
        'vue/one-component-per-file': 'off',
        'vue/html-closing-bracket-newline': 'off',
        'vue/max-attributes-per-line': 'off',
        'vue/multiline-html-element-content-newline': 'off',
        'vue/singleline-html-element-content-newline': 'off',
        'vue/attribute-hyphenation': 'off',
        'vue/require-default-prop': 'off',
        'vue/html-self-closing': [
            'error',
            {
                html: {
                    void: 'always',
                    normal: 'never',
                    component: 'always',
                },
                svg: 'always',
                math: 'always',
            },
        ],
        // Enable vue/script-setup-uses-vars rule
        'vue/script-setup-uses-vars': 'error',
    }
};
