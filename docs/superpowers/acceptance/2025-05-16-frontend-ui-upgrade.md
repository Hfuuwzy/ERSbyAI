# Acceptance Criteria: Phase 3 Frontend UI/UX Upgrade

**Spec:** `docs/superpowers/specs/2025-05-16-frontend-ui-upgrade-design.md`
**Date:** 2025-05-16
**Status:** Approved (autonomous mode)

---

## Design System Criteria

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-001 | Design system CSS variables exist | Logic | Project built | `design-system.scss` contains color, spacing, shadow variables |
| AC-002 | Animation keyframes defined | Logic | Project built | `animations.scss` contains fadeUp, gradientShift, hover-lift keyframes |
| AC-003 | Card component follows design spec | UI interaction | Browser running | All cards have rounded-2xl (16px), white bg, shadow, and hover-lift effect |
| AC-004 | Button variants exist | UI interaction | Browser running | Primary: gradient bg; Secondary: white with border; both have hover states |
| AC-005 | Tag skill colors per category | UI interaction | Browser running | Frontend tags: blue; Backend: purple; Product: pink; Others: gray |

## Layout Components Criteria

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-006 | GlassCard component renders | UI interaction | Component mounted | GlassCard has backdrop-blur, semi-transparent bg, border |
| AC-007 | GradientButton component renders | UI interaction | Component mounted | Button has gradient bg, hover glow effect |
| AC-008 | JobCard component displays job info | UI interaction | Job data passed | Shows: title, salary, tags, company logo/name/stage |
| AC-009 | SearchBox component with search button | UI interaction | Component mounted | Input with search icon + gradient search button |
| AC-010 | EmptyState component for empty lists | UI interaction | Empty data passed | Shows illustration, message, action button |

## Page: Front.vue (Layout Shell)

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-011 | Header is fixed position | UI interaction | Page scrolled | Header stays at top, has backdrop-blur, shadow on scroll |
| AC-012 | Header navigation links work | UI interaction | Click nav link | Router navigates to correct route |
| AC-013 | Mobile hamburger menu toggles | UI interaction | Screen < 640px | Menu icon click reveals mobile nav drawer |
| AC-014 | Footer displays four columns | UI interaction | Page loaded | Columns: 关于我们, 求职者, 企业, 联系方式 |
| AC-015 | User dropdown menu shows logout | UI interaction | User logged in | Click avatar shows dropdown with logout option |

## Page: Home.vue (Homepage)

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-016 | Hero gradient background animates | UI interaction | Page loaded | Gradient background has flowing animation (8s cycle) |
| AC-017 | Hero search box centered with button | UI interaction | Page loaded | Search input 600px wide, gradient search button beside it |
| AC-018 | Featured jobs horizontal scroll | UI interaction | Job data exists | Cards in horizontal scroll container, arrow buttons work |
| AC-019 | Bento Grid ad layout displays 6 ads | UI interaction | Ad data exists | 6 ad images in bento grid layout (2x3 irregular) |
| AC-020 | Hot jobs tab switching | UI interaction | Click tab | Active tab has sliding indicator, content updates |
| AC-021 | Job cards in hot jobs are clickable | UI interaction | Click job card | Navigates to position detail page |

## Page: PositionDetail.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-022 | Header shows job info with gradient bg | UI interaction | Page loaded | Shows: title, salary highlighted, location/exp/edu/type icons |
| AC-023 | Action buttons present | UI interaction | User is USER role | Collect button (purple), Submit button (gradient) |
| AC-024 | Left column shows job description | UI interaction | Page loaded | Content: description section, skill tags, rich text |
| AC-025 | Right sticky card shows company info | UI interaction | Page loaded | Card sticks on scroll, shows: logo, name, stage, industry, size |
| AC-026 | Similar jobs section at bottom | UI interaction | Related jobs exist | Horizontal scroll of similar position cards |

## Page: Search.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-027 | Search box prominent with hot tags | UI interaction | Page loaded | Large search input, hot search tags below |
| AC-028 | Filter bar with dropdowns | UI interaction | Page loaded | Filters: city, salary, experience, education |
| AC-029 | Search results in grid | UI interaction | Search executed | 3-column grid on desktop, JobCard components |
| AC-030 | Empty state for no results | UI interaction | No matches | Shows EmptyState with friendly message |
| AC-031 | Clear filters button works | UI interaction | Filters applied | Click clears all filters, reloads results |

## Page: Resume.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-032 | Resume list displays cards | UI interaction | Resumes exist | Each card: name, updated date, salary, exp |
| AC-033 | Resume actions available | UI interaction | Card rendered | Edit, Delete, Submit buttons per resume |
| AC-034 | Create resume button | UI interaction | Click button | Navigates to ResumeEdit page |
| AC-035 | Empty state for no resumes | UI interaction | No resumes | Shows illustration + "创建第一份简历" button |

## Page: ResumeEdit.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-036 | Form sections organized | UI interaction | Page loaded | Sections: 基本信息, 教育经历, 工作经历, 项目经历 |
| AC-037 | Add experience opens modal | UI interaction | Click add button | Modal opens for adding/editing experience |
| AC-038 | Save and preview buttons | UI interaction | Page loaded | Top-right buttons: Save (primary), Preview (secondary) |
| AC-039 | Form validation shows errors | UI interaction | Submit invalid | Invalid fields show error messages |
| AC-040 | Experience timeline displays | UI interaction | Experience exists | Entries shown in chronological order with edit/delete |

## Page: Submit.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-041 | Tab filter for statuses | UI interaction | Page loaded | Tabs: 全部, 待处理, 面试中, 已录用, 已拒绝 |
| AC-042 | Submit records display cards | UI interaction | Records exist | Card shows: position, company, status tag, salary |
| AC-043 | AI score shown if available | UI interaction | AI score exists | Shows: "AI评估: 85分" on card |
| AC-044 | View toggle list/timeline | UI interaction | Toggle clicked | Switches between card list and timeline view |

## Page: Collect.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-045 | Collection grid displays | UI interaction | Collections exist | Grid of JobCard components |
| AC-046 | Unfavorite on star hover | UI interaction | Hover star | Shows unfavorite option, click removes |
| AC-047 | Empty state with call-to-action | UI interaction | No collections | Shows "还没有收藏职位" + link to home |

## Page: Person.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-048 | Profile card at top | UI interaction | User loaded | Shows: large avatar, name, title, contact |
| AC-049 | Info sections organized | UI interaction | Page loaded | Sections: 基本信息, 求职意向 |
| AC-050 | Edit profile functionality | UI interaction | Click edit | Opens edit form or modal |

## Page: Login.vue / Register.vue

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-051 | Full-screen gradient background | UI interaction | Page loaded | Gradient covers full viewport |
| AC-052 | Glass card centered | UI interaction | Page loaded | Card has backdrop-blur, centered horizontally and vertically |
| AC-053 | Form inputs styled | UI interaction | Page loaded | Inputs have rounded corners, focus state with primary color |
| AC-054 | Switch between login/register | UI interaction | Click link | Navigates between Login and Register pages |

## Responsive Criteria

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-055 | Mobile hamburger menu | UI interaction | Screen < 640px | Nav items collapse into hamburger menu |
| AC-056 | Touch targets minimum size | UI interaction | Mobile view | All interactive elements >= 44px touch target |
| AC-057 | Grid adapts to columns | UI interaction | Resize window | Mobile: 1 col, Tablet: 2 col, Desktop: 3-4 col |
| AC-058 | Text readable on mobile | UI interaction | Mobile view | Font sizes scale appropriately, no overflow |
| AC-059 | Horizontal scroll on mobile | UI interaction | Mobile view | Featured jobs horizontal scroll works with touch |

## Performance Criteria

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-060 | Animations use GPU acceleration | UI interaction | Animations running | transform and opacity used, not layout properties |
| AC-061 | No layout shift on load | UI interaction | Page loading | Cumulative Layout Shift < 0.1 |
| AC-062 | Images have placeholders | UI interaction | Images loading | Skeleton or placeholder shown while loading |

## Functional Preservation Criteria

| ID | Description | Test Type | Preconditions | Expected Result |
|----|-------------|-----------|---------------|-----------------|
| AC-063 | All API calls remain functional | API | User actions | Search, submit, collect, resume CRUD still work |
| AC-064 | Router navigation intact | UI interaction | Navigate pages | All routes work, no 404 errors for valid paths |
| AC-065 | User auth state preserved | Logic | Login completed | User stays logged in, avatar shows correctly |
| AC-066 | Form submissions work | API | Submit form | Login, register, resume edit forms submit successfully |

---

**Total Criteria:** 66

**Next Step:** Proceed to `writing-plans` to create implementation plan.
