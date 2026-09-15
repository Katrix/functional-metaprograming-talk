import { defineAppSetup } from '@slidev/types'
import { slides } from '#slidev/slides'
import { flags, setFlags } from './flags'

function getSlide(no: unknown) {
  if (typeof no !== 'string') {
    return undefined
  }
  return slides.value.find(s => s.no === Number(no) || s.meta.slide?.frontmatter.routeAlias === no)
}

export default defineAppSetup(({ app, router }) => {
  app.config.globalProperties.$flags = flags

  router.beforeEach((to, from) => {
    const fromSlide = getSlide(from.params.no)
    const toSlide = getSlide(to.params.no)
    const returnTo = fromSlide?.meta.slide?.frontmatter.returnTo

    if (fromSlide && toSlide && returnTo && toSlide.no === fromSlide.no + 1) {
      const prefix = to.path.startsWith('/presenter/') ? '/presenter' : ''
      return { path: `${prefix}/${returnTo}`, replace: true }
    }
  })

  router.afterEach((to) => {
    setFlags(getSlide(to.params.no)?.meta.slide?.frontmatter.setFlag)
  })
})
