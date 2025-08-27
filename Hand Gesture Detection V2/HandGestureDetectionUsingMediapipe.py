import cv2
import mediapipe as mp

# Initialize MediaPipe Hand model
mp_hands = mp.solutions.hands
mp_draw = mp.solutions.drawing_utils
hands = mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.7)

# Open webcam
cap = cv2.VideoCapture(0)

def count_fingers(hand_landmarks):
    """ Count raised fingers based on landmark positions """
    finger_tips = [8, 12, 16, 20]  # Index, Middle, Ring, Pinky
    thumb_tip = 4
    fingers = []

    # Check fingers (except thumb)
    for tip in finger_tips:
        if hand_landmarks.landmark[tip].y < hand_landmarks.landmark[tip - 2].y:
            fingers.append(1)  # finger is open
        else:
            fingers.append(0)  # finger is closed

    # Check thumb (horizontal check)
    if hand_landmarks.landmark[thumb_tip].x < hand_landmarks.landmark[thumb_tip - 2].x:
        fingers.append(1)
    else:
        fingers.append(0)

    return fingers.count(1)

while True:
    success, img = cap.read()
    img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    result = hands.process(img_rgb)

    if result.multi_hand_landmarks:
        for handLms in result.multi_hand_landmarks:
            mp_draw.draw_landmarks(img, handLms, mp_hands.HAND_CONNECTIONS)

            finger_count = count_fingers(handLms)

            # Print text based on gesture
            if finger_count == 0:
                text = "Fist"
            elif finger_count == 1:
                text = "One"
            elif finger_count == 2:
                text = "Peace ✌"
            elif finger_count == 5:
                text = "Five Finger"
            else:
                text = f"{finger_count} Fingers"

            cv2.putText(img, text, (10, 50), cv2.FONT_HERSHEY_SIMPLEX,
                        1, (0, 255, 0), 2)

    cv2.imshow("Hand Gesture Detection", img)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
