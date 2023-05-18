import { instantiate } from './mood-tracker.uninstantiated.mjs';

await wasmSetup;
instantiate({ skia: Module['asm'] });