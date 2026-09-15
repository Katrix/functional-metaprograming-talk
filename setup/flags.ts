import { reactive } from 'vue'

export const flags = reactive(new Set<string>())

export function setFlags(value: string | string[] | undefined) {
  if (!value) {
    return
  }

  for (const flag of Array.isArray(value) ? value : [value]) {
    flags.add(flag)
  }
}