# 업로드된 테스트 데이터 확인 스크립트
Write-Host "=== 업로드된 테스트 데이터 확인 ===" -ForegroundColor Green

try {
    Write-Host "`n1. 전체 Market 목록 조회 중..." -ForegroundColor Yellow
    $allMarkets = Invoke-RestMethod -Uri "http://localhost:8080/api/market/list" -Method GET
    Write-Host "전체 Market 개수: $($allMarkets.totalCount)" -ForegroundColor Cyan
    
    Write-Host "`n2. '테스트' 키워드로 검색 중..." -ForegroundColor Yellow
    $testMarkets = Invoke-RestMethod -Uri "http://localhost:8080/api/market/search?keyword=테스트" -Method GET
    Write-Host "테스트 시장 개수: $($testMarkets.totalCount)" -ForegroundColor Cyan
    
    if ($testMarkets.data -and $testMarkets.data.Count -gt 0) {
        Write-Host "`n=== CSV에서 업로드된 테스트 시장들 ===" -ForegroundColor Magenta
        foreach ($market in $testMarkets.data) {
            if ($market.marketName -like "*테스트시장*") {
                Write-Host "- $($market.marketName) ($($market.marketLocal))" -ForegroundColor White
                Write-Host "  주소: $($market.marketAddress)" -ForegroundColor Gray
                Write-Host "  등록일: $($market.createdDate)" -ForegroundColor Gray
                Write-Host ""
            }
        }
    }
    
    Write-Host "3. 'JSON' 키워드로 검색 중..." -ForegroundColor Yellow
    $jsonMarkets = Invoke-RestMethod -Uri "http://localhost:8080/api/market/search?keyword=JSON" -Method GET
    Write-Host "JSON 시장 개수: $($jsonMarkets.totalCount)" -ForegroundColor Cyan
    
    if ($jsonMarkets.data -and $jsonMarkets.data.Count -gt 0) {
        Write-Host "`n=== JSON에서 업로드된 테스트 시장들 ===" -ForegroundColor Magenta
        foreach ($market in $jsonMarkets.data) {
            Write-Host "- $($market.marketName) ($($market.marketLocal))" -ForegroundColor White
            Write-Host "  주소: $($market.marketAddress)" -ForegroundColor Gray
            Write-Host "  등록일: $($market.createdDate)" -ForegroundColor Gray
            Write-Host ""
        }
    }
    
    Write-Host "=== 테스트 데이터 확인 완료! ===" -ForegroundColor Green
    
} catch {
    Write-Host "오류 발생: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "서버가 실행 중인지 확인해주세요." -ForegroundColor Yellow
}