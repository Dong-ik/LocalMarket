/**
 * LocalMarket 공통 JavaScript 함수들
 */

// 전역 변수
let currentMemberId = null;

$(document).ready(function() {
    // 세션에서 멤버 ID 가져오기
    getCurrentMember();
    
    // 토스트 알림 초기화
    initializeToast();
});

/**
 * 현재 로그인한 멤버 정보 가져오기
 */
function getCurrentMember() {
    // 실제로는 세션에서 가져와야 함
    // 여기서는 임시로 설정
    currentMemberId = getCookie('memberId');
}

/**
 * 쿠키 값 가져오기
 */
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

/**
 * 관심등록 토글 함수
 * @param {string} type - 'MARKET' 또는 'STORE'
 * @param {number} targetId - 대상 ID
 * @param {Element} element - 클릭된 버튼 엘리먼트
 */
function toggleFavorite(type, targetId, element) {
    if (!currentMemberId) {
        showToast('로그인이 필요합니다.', 'warning');
        location.href = '/members/login';
        return;
    }
    
    const $btn = $(element);
    const $icon = $btn.find('i');
    const isActive = $btn.hasClass('active');
    
    // UI 즉시 업데이트 (사용자 경험 향상)
    if (isActive) {
        $btn.removeClass('active');
        $icon.removeClass('fas').addClass('far');
    } else {
        $btn.addClass('active');
        $icon.removeClass('far').addClass('fas');
    }
    
    // 서버에 요청
    $.ajax({
        url: '/api/favorites/toggle',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            memberId: currentMemberId,
            favoriteType: type,
            targetId: targetId
        }),
        success: function(response) {
            if (response.success) {
                const message = response.isFavorite ? '관심등록되었습니다.' : '관심등록이 해제되었습니다.';
                showToast(message, 'success');
                
                // 관심등록 카운트 업데이트
                updateFavoriteCount(targetId, response.favoriteCount);
            } else {
                // 실패 시 UI 되돌리기
                revertFavoriteUI($btn, $icon, isActive);
                showToast('처리 중 오류가 발생했습니다.', 'error');
            }
        },
        error: function() {
            // 오류 시 UI 되돌리기
            revertFavoriteUI($btn, $icon, isActive);
            showToast('네트워크 오류가 발생했습니다.', 'error');
        }
    });
}

/**
 * 관심등록 UI 되돌리기
 */
function revertFavoriteUI($btn, $icon, wasActive) {
    if (wasActive) {
        $btn.addClass('active');
        $icon.removeClass('far').addClass('fas');
    } else {
        $btn.removeClass('active');
        $icon.removeClass('fas').addClass('far');
    }
}

/**
 * 관심등록 카운트 업데이트
 */
function updateFavoriteCount(targetId, count) {
    const $countElement = $(`.favorite-count[data-target-id="${targetId}"] span`);
    if ($countElement.length > 0) {
        $countElement.text(count);
    }
}

/**
 * 장바구니에 상품 추가
 * @param {number} productId - 상품 ID
 * @param {number} quantity - 수량 (기본값: 1)
 */
function addToCart(productId, quantity = 1) {
    if (!currentMemberId) {
        showToast('로그인이 필요합니다.', 'warning');
        location.href = '/members/login';
        return;
    }
    
    $.ajax({
        url: '/api/cart/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            memberId: currentMemberId,
            productId: productId,
            quantity: quantity
        }),
        success: function(response) {
            if (response.success) {
                showToast('장바구니에 추가되었습니다.', 'success');
                updateCartCount();
            } else {
                showToast(response.message || '장바구니 추가 중 오류가 발생했습니다.', 'error');
            }
        },
        error: function() {
            showToast('네트워크 오류가 발생했습니다.', 'error');
        }
    });
}

/**
 * 장바구니 개수 업데이트
 */
function updateCartCount() {
    $.ajax({
        url: '/api/cart/count',
        type: 'GET',
        success: function(response) {
            if (response.success) {
                const $cartBadge = $('.cart-count');
                if (response.count > 0) {
                    $cartBadge.text(response.count).show();
                } else {
                    $cartBadge.hide();
                }
            }
        },
        error: function() {
            // 에러 시 배지 숨김
            $('.cart-count').hide();
        }
    });
}

/**
 * 관심 개수 업데이트
 */
function updateFavoriteCount() {
    $.ajax({
        url: '/api/favorites/count',
        type: 'GET',
        success: function(response) {
            if (response.success) {
                const $favoriteBadge = $('.favorite-count');
                if (response.count > 0) {
                    $favoriteBadge.text(response.count).show();
                } else {
                    $favoriteBadge.hide();
                }
            }
        },
        error: function() {
            // 에러 시 배지 숨김
            $('.favorite-count').hide();
        }
    });
}

/**
 * 토스트 알림 초기화
 */
function initializeToast() {
    if ($('#toast-container').length === 0) {
        $('body').append(`
            <div id="toast-container" style="
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 9999;
                max-width: 300px;
            "></div>
        `);
    }
}

/**
 * 토스트 메시지 표시
 * @param {string} message - 메시지
 * @param {string} type - 'success', 'error', 'warning', 'info'
 */
function showToast(message, type = 'info') {
    const colors = {
        success: '#28a745',
        error: '#dc3545',
        warning: '#ffc107',
        info: '#17a2b8'
    };
    
    const icons = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
    };
    
    const toastId = 'toast-' + Date.now();
    const $toast = $(`
        <div id="${toastId}" class="toast" style="
            background: white;
            border-left: 4px solid ${colors[type]};
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            transform: translateX(400px);
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
        ">
            <i class="${icons[type]}" style="color: ${colors[type]}; margin-right: 10px; font-size: 18px;"></i>
            <span style="flex: 1; color: #333;">${message}</span>
            <button onclick="hideToast('${toastId}')" style="
                background: none;
                border: none;
                font-size: 18px;
                color: #999;
                cursor: pointer;
                margin-left: 10px;
            ">&times;</button>
        </div>
    `);
    
    $('#toast-container').append($toast);
    
    // 애니메이션으로 표시
    setTimeout(() => {
        $toast.css('transform', 'translateX(0)');
    }, 100);
    
    // 3초 후 자동 제거
    setTimeout(() => {
        hideToast(toastId);
    }, 3000);
}

/**
 * 토스트 메시지 숨기기
 */
function hideToast(toastId) {
    const $toast = $('#' + toastId);
    $toast.css('transform', 'translateX(400px)');
    setTimeout(() => {
        $toast.remove();
    }, 300);
}

/**
 * 페이지 로딩 표시
 */
function showLoading() {
    if ($('#loading-overlay').length === 0) {
        $('body').append(`
            <div id="loading-overlay" style="
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                z-index: 9998;
                display: flex;
                align-items: center;
                justify-content: center;
            ">
                <div style="
                    background: white;
                    padding: 30px;
                    border-radius: 10px;
                    text-align: center;
                ">
                    <i class="fas fa-spinner fa-spin" style="font-size: 24px; color: #3498db; margin-bottom: 10px;"></i>
                    <div>처리중...</div>
                </div>
            </div>
        `);
    }
}

/**
 * 페이지 로딩 숨기기
 */
function hideLoading() {
    $('#loading-overlay').remove();
}

/**
 * 숫자 포맷팅 (천단위 콤마)
 */
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

/**
 * 가격 포맷팅
 */
function formatPrice(price) {
    return formatNumber(price) + '원';
}

/**
 * 모달 열기
 */
function openModal(modalId) {
    $('#' + modalId).css('display', 'flex').hide().fadeIn(300);
    $('body').addClass('modal-open');
}

/**
 * 모달 닫기
 */
function closeModal(modalId) {
    $('#' + modalId).fadeOut(300);
    $('body').removeClass('modal-open');
}

/**
 * 확인 다이얼로그
 */
function confirmDialog(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// 페이지 로드 시 장바구니 및 관심 개수 업데이트
$(document).ready(function() {
    updateCartCount();
    updateFavoriteCount();
});