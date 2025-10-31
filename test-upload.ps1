Add-Type -AssemblyName System.Net.Http

# CSV 파일 업로드 테스트
function Test-CsvUpload {
    Write-Host "=== CSV 파일 업로드 테스트 ===" -ForegroundColor Green
    
    try {
        $httpClient = New-Object System.Net.Http.HttpClient
        $form = New-Object System.Net.Http.MultipartFormDataContent
        
        # 파일 읽기
        $fileBytes = [System.IO.File]::ReadAllBytes("test_markets.csv")
        $fileContent = New-Object System.Net.Http.ByteArrayContent $fileBytes
        
        # Content-Disposition 헤더 설정
        $disposition = New-Object System.Net.Http.Headers.ContentDispositionHeaderValue "form-data"
        $disposition.Name = '"file"'
        $disposition.FileName = '"test_markets.csv"'
        $fileContent.Headers.ContentDisposition = $disposition
        $fileContent.Headers.ContentType = [System.Net.Http.Headers.MediaTypeHeaderValue]::Parse("text/csv")
        
        $form.Add($fileContent)
        
        # POST 요청
        $response = $httpClient.PostAsync("http://localhost:8080/api/market/import-from-csv", $form).Result
        $responseContent = $response.Content.ReadAsStringAsync().Result
        
        Write-Host "응답 상태: $($response.StatusCode)" -ForegroundColor Yellow
        Write-Host "응답 내용: $responseContent" -ForegroundColor Cyan
        
        $httpClient.Dispose()
        return $response.StatusCode -eq 200
    }
    catch {
        Write-Host "오류 발생: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# JSON 파일 업로드 테스트
function Test-JsonUpload {
    Write-Host "`n=== JSON 파일 업로드 테스트 ===" -ForegroundColor Green
    
    try {
        $httpClient = New-Object System.Net.Http.HttpClient
        $form = New-Object System.Net.Http.MultipartFormDataContent
        
        # 파일 읽기
        $fileBytes = [System.IO.File]::ReadAllBytes("test_markets.json")
        $fileContent = New-Object System.Net.Http.ByteArrayContent $fileBytes
        
        # Content-Disposition 헤더 설정
        $disposition = New-Object System.Net.Http.Headers.ContentDispositionHeaderValue "form-data"
        $disposition.Name = '"file"'
        $disposition.FileName = '"test_markets.json"'
        $fileContent.Headers.ContentDisposition = $disposition
        $fileContent.Headers.ContentType = [System.Net.Http.Headers.MediaTypeHeaderValue]::Parse("application/json")
        
        $form.Add($fileContent)
        
        # POST 요청
        $response = $httpClient.PostAsync("http://localhost:8080/api/market/import-from-json", $form).Result
        $responseContent = $response.Content.ReadAsStringAsync().Result
        
        Write-Host "응답 상태: $($response.StatusCode)" -ForegroundColor Yellow
        Write-Host "응답 내용: $responseContent" -ForegroundColor Cyan
        
        $httpClient.Dispose()
        return $response.StatusCode -eq 200
    }
    catch {
        Write-Host "오류 발생: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# 테스트 실행
Write-Host "파일 업로드 기능 테스트를 시작합니다..." -ForegroundColor Magenta
Write-Host "애플리케이션이 http://localhost:8080 에서 실행중인지 확인해주세요.`n"

$csvResult = Test-CsvUpload
$jsonResult = Test-JsonUpload

Write-Host "`n=== 테스트 결과 요약 ===" -ForegroundColor Magenta
Write-Host "CSV 업로드: $(if($csvResult) {'성공'} else {'실패'})" -ForegroundColor $(if($csvResult) {'Green'} else {'Red'})
Write-Host "JSON 업로드: $(if($jsonResult) {'성공'} else {'실패'})" -ForegroundColor $(if($jsonResult) {'Green'} else {'Red'})

if ($csvResult -and $jsonResult) {
    Write-Host "`n모든 테스트가 성공했습니다! 🎉" -ForegroundColor Green
} else {
    Write-Host "`n일부 테스트가 실패했습니다. 애플리케이션 상태를 확인해주세요." -ForegroundColor Yellow
}