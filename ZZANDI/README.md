# 아이디어8 [Step By Step]팀 프로젝트 “ZZANDI” 

<p align="center"><img src="https://user-images.githubusercontent.com/72914519/184625540-53b152f4-cc92-4a50-b95c-4d88411b6a01.png" width="460" height="300"></p>

## ✍커밋 컨벤션


EX)
```bash
✨feat : 로그인 기능 구현

로그인 인증 및 인가 & 핸들러 추가

resolves : #123
ref : #456
related : #78, #90
```
❗  :  전후로 항상 space 포함 & 첫글자는 소문자로
- **`feat` : 새로운 기능 추가 / 수정 / 삭제** 
- **`fix` : 버그 수정**
- **`docs` : 문서 & 주석 추가 / 수정 / 삭제 (기능 변화가 없을 때)**
- **`style` : UI 관련 작업**
- **`refactor` : 코드 리펙토링**
- **`test` : 테스트 코드, 리펙토링 테스트 코드 추가**
- **`chore` : 빌드 업무 수정, 패키지 매니저 수정**
- **`del` : 불필요한 코드 & 파일 삭제**

- **`fixes`: 이슈 수정중 (아직 해결되지 않은 경우)**
- **`resolves`: 이슈를 해결했을 때 사용**
- **`ref`: 참고할 이슈가 있을 때 사용**
- **`related to`: 해당 커밋에 관련된 이슈번호 (아직 해결되지 않은 경우)**

## 😗 Git Emoji
|:-:|:-:|:-:|  
|✨|`:sparkles:`|새 기능|
|🎨|`:art:`|코드의 구조/형태 개선|
|🔥|`:fire:`|코드/파일 삭제|
|📝|`:memo:`|문서 추가/수정|
|💡|`:bulb:`|주석 추가/수정|
|🙈|`:see_no_evil:`|.gitignore 추가/수정|
|➕|`:heavy_plus_sign:`|의존성 추가|
|➖|`:heavy_minus_sign:`|의존성 제거|
|🔀|`:twisted_rightwards_arrows:`|브랜치 병합|
|💄|`:lipstick:`|UI/스타일 파일 추가/수정|
|🐛|`:bug:`|버그 수정|
|✅|`:white_check_mark:`|테스트 추가/수정|
|♻️|`:recycle:`|코드 리팩토링|

## 📌 Branch


- **`master`: 배포가 가능한 상태의 안정적인 브랜치**
- **`develop` : 개발이 진행되는 브랜치**
- **`feature` : 기능 개발 후 Pull-Request 생성하기 → 확인 후 merge → dev 브랜치.**
- **`hotfix` : 배포된 main에서의 오류 수정을 위한 브랜치**
