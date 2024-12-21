import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="larissAsstApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="paymentMethod">
              <Translate contentKey="larissAsstApp.payment.paymentMethod">Payment Method</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentMethod}</dd>
          <dt>
            <span id="cardNumber">
              <Translate contentKey="larissAsstApp.payment.cardNumber">Card Number</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.cardNumber}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="larissAsstApp.payment.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.expirationDate ? <TextFormat value={paymentEntity.expirationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="cvv">
              <Translate contentKey="larissAsstApp.payment.cvv">Cvv</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.cvv}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
