Simple API 문서 입니다.
___

# 상태코드<a name="status">&nbsp;</a>
다음 상태코드는 API 명령 수행 중에 발생한 에러 코드입니다.
API에서 에러가 발생할 경우 응답 데이터의 status 필드에 에러 코드가 저장됩니다.
API 실행 중에 에러가 발생해도 HTTP 정상 반환(200)되고 있으며 에러 발생 여부 확인은 응답 메시지에서 status가 1 이상인 경우 에러라고 판단합니다.
Status를 통해서 에러 발생 여부를 확인하고 status 값을 가지고 아래 Status Code에서 확인하시기 바랍니다.
> **Note**  : 추후 모든 에러를 HTTP 500 에러(internal error)로 리턴하고, status에서 자세한 에러 상태를 확인하는게 좋다고 판단되면 변경할 예정입니다.
