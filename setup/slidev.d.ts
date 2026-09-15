import type { SlideInfo } from '@slidev/types'

declare module 'vue-router' {
  interface RouteMeta {
    slide?: Omit<SlideInfo, 'source' | 'frontmatter'> & {
      frontmatter: SlideInfo['frontmatter'] & {
        returnTo?: string | number
        section?: string
        subsection?: string
        setFlag?: string | string[]
      }
      noteHTML: string
      filepath: string
    }
  }
}

declare module 'vue' {
  interface ComponentCustomProperties {
    $flags: Set<string>
  }
}
