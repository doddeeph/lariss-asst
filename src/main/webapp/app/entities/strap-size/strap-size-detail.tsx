import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './strap-size.reducer';

export const StrapSizeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const strapSizeEntity = useAppSelector(state => state.strapSize.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="strapSizeDetailsHeading">
          <Translate contentKey="larissAsstApp.strapSize.detail.title">StrapSize</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{strapSizeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.strapSize.name">Name</Translate>
            </span>
          </dt>
          <dd>{strapSizeEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="larissAsstApp.strapSize.value">Value</Translate>
            </span>
          </dt>
          <dd>{strapSizeEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/strap-size" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/strap-size/${strapSizeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StrapSizeDetail;
